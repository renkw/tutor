/*
 * File   ServiceServlet.java
 * Create 2012/12/25
 * Copyright (c) change-v.com 2012
 */
package com.changev.tutor.web;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import org.springframework.beans.factory.BeanFactory;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.ModelFactory;
import com.changev.tutor.model.UserModel;
import com.db4o.ObjectSet;
import com.google.gson.Gson;

/**
 * <p>
 * 执行系统服务。
 * </p>
 * 
 * @author ren
 * 
 */
public class ServiceServlet extends HttpServlet {

	private static final long serialVersionUID = 6483362567814108674L;

	private static final Logger logger = Logger.getLogger(ServiceServlet.class);
	private static final Logger authLogger = Logger
			.getLogger(Tutor.AUTH_LOGGER_NAME);

	static final String PUBLIC_SERVICES = "publicServices";
	static final String EXPIRATION = "expiration";
	static final String TOKEN_CLEAN_PERIOD = "tokenCleanPeriod";

	static final String TUTOR_UDID_HEADER = "X-Tutor-UDID";
	static final String TUTOR_USER_HEADER = "X-Tutor-User";
	static final String TUTOR_TOKEN_HEADER = "X-Tutor-Token";
	static final String TUTOR_AUTH_HEADER = "X-Tutor-Auth";

	List<String> publicServices;
	long expiration;

	Map<String, AuthInfo> tokens = new ConcurrentHashMap<>();

	@Override
	public void init() throws ServletException {
		if (logger.isTraceEnabled())
			logger.trace("[init] called");

		String publicServices = getInitParameter(PUBLIC_SERVICES);
		String expiration = getInitParameter(EXPIRATION);
		String tokenCleanPeriod = getInitParameter(TOKEN_CLEAN_PERIOD);
		if (logger.isDebugEnabled()) {
			logger.debug("[init] publicServices = " + publicServices);
			logger.debug("[init] expiration = " + expiration);
			logger.debug("[init] tokenCleanPeriod = " + tokenCleanPeriod);
		}

		this.publicServices = Collections.emptyList();
		if (StringUtils.isNotEmpty(publicServices)) {
			this.publicServices = new ArrayList<>();
			for (String s : StringUtils.split(publicServices, ',')) {
				if (!(s = s.trim()).isEmpty())
					this.publicServices.add(s);
			}
		}

		this.expiration = StringUtils.isEmpty(expiration) ? Long.MAX_VALUE
				: Long.parseLong(expiration);

		if (StringUtils.isNotEmpty(expiration)
				&& StringUtils.isNotEmpty(tokenCleanPeriod)) {
			// remove all expired tokens
			final long period = Long.parseLong(tokenCleanPeriod);
			Thread tokenCleaner = new Thread("tokenCleaner") {
				@Override
				public void run() {
					try {
						sleep(period);
						long time = System.currentTimeMillis();
						Iterator<Map.Entry<String, AuthInfo>> iter = tokens
								.entrySet().iterator();
						while (iter.hasNext()) {
							AuthInfo info = iter.next().getValue();
							if (time > info.expiration) {
								if (logger.isDebugEnabled())
									logger.debug("[run] expired:" + info);
								iter.remove();
							}
						}
					} catch (InterruptedException e) {
						logger.error("[run] token cleaner interrupted", e);
					}
				}
			};
			tokenCleaner.setDaemon(true);
			tokenCleaner.start();
			if (logger.isDebugEnabled())
				logger.debug("[init] start token cleaner");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (logger.isTraceEnabled())
			logger.trace("[doPost] called");

		// set udid info
		String udid = req.getHeader(TUTOR_UDID_HEADER);
		if (StringUtils.isNotEmpty(udid))
			NDC.push(udid);

		try {
			String serviceName = getServiceName(req.getPathInfo());
			if (logger.isDebugEnabled())
				logger.debug("[doPost] serviceName = " + serviceName);

			// get service
			BeanFactory beanFactory = Tutor.getBeanFactory();
			Service<?> service = null;
			if (serviceName != null && beanFactory.containsBean(serviceName))
				service = (Service<?>) beanFactory.getBean(serviceName);
			if (logger.isDebugEnabled())
				logger.debug("[doPost] service = " + service);
			if (service == null) {
				// service not found. set error 404
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			// login
			UserModel userModel;
			if (publicServices.contains(serviceName)) {
				// public service
				userModel = null;
				if (logger.isDebugEnabled())
					logger.debug("[doPost] public service");
			} else {
				userModel = getUserModel(serviceName, req, resp);
				if (logger.isDebugEnabled())
					logger.debug("[doPost] userModel = " + userModel);
				if (userModel == null)
					return;
			}

			// run service
			try {
				if (userModel != null) {
					authLogger.info(MessageFormat.format(
							Tutor.AUTH_LOGIN_FORMAT, userModel.getUsername(),
							serviceName));
				}
				runService(service, userModel, req, resp);
			} finally {
				if (userModel != null) {
					authLogger.info(MessageFormat.format(
							Tutor.AUTH_LOGOUT_FORMAT, userModel.getUsername(),
							serviceName));
				}
			}
		} finally {
			// remove udid info
			if (StringUtils.isNotEmpty(udid))
				NDC.pop();
		}
	}

	/**
	 * <p>
	 * 取得登录用户。
	 * </p>
	 * 
	 * <p>
	 * 此方法会返回客户端Http错误状态：
	 * <ul>
	 * <li><strong>{@link HttpServletResponse#SC_BAD_REQUEST 400}</strong>
	 * 当X-Tutor-User头不存在，或格式不符时；</li>
	 * <li><strong>{@link HttpServletResponse#SC_UNAUTHORIZED 401}</strong>
	 * 当X-Tutor-Auth头存在，但对应用户不存在或令牌不符（安全码可能过期）时；</li>
	 * <li><strong>{@link HttpServletResponse#SC_FORBIDDEN 403}</strong>
	 * 当X-Tutor-Auth头存在，但请求认证的服务名不一致时；</li>
	 * </ul>
	 * </p>
	 * 
	 * @param serviceName
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	protected UserModel getUserModel(String serviceName,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (logger.isDebugEnabled())
			logger.debug("[getUserModel] called");

		String user = request.getHeader(TUTOR_USER_HEADER);
		String auth = request.getHeader(TUTOR_AUTH_HEADER);
		if (logger.isDebugEnabled()) {
			logger.debug("[getUserModel] X-Tutor-User = " + user);
			logger.debug("[getUserModel] X-Tutor-Auth = " + auth);
		}

		if (StringUtils.isEmpty(user)) {
			// missing required header. send 400
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}

		AuthInfo info;
		if (StringUtils.isEmpty(auth)) {
			// decrypt
			String content;
			try {
				Cipher cipher = Cipher.getInstance("AES");
				cipher.init(Cipher.DECRYPT_MODE, Tutor.AES_KEY);
				content = new String(cipher.doFinal(user.getBytes()));
				if (logger.isDebugEnabled())
					logger.debug("[getUserModel] content = " + content);
			} catch (InvalidKeyException | NoSuchAlgorithmException
					| NoSuchPaddingException | IllegalBlockSizeException
					| BadPaddingException e) {
				logger.error("[getUserModel] AES decrypt failed", e);
				throw new RuntimeException(e);
			}

			// username::clientToken
			int sep = content.indexOf("::");
			if (sep == -1) {
				// illegal usercode. send 400
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			} else {
				String serviceToken = RandomStringUtils.randomAscii(100);
				info = new AuthInfo(content.substring(0, sep), serviceName,
						serviceToken, content.substring(sep + 2),
						(System.currentTimeMillis() + expiration));
				if (logger.isDebugEnabled())
					logger.debug("[getUserModel] create authInfo = " + info);
				tokens.put(user, info);
				response.setHeader(TUTOR_TOKEN_HEADER, serviceToken);
				response.getOutputStream().close();
			}
			return null;
		}

		info = tokens.remove(user);
		if (logger.isDebugEnabled())
			logger.debug("[getUserModel] get authInfo = " + info);
		if (info == null || !serviceName.equals(info.serviceName)) {
			// forbidden. send 403
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}

		ObjectSet<UserModel> userSet = Tutor.getCurrentContainer()
				.queryByExample(ModelFactory.getUserExample(info.username));
		if (!userSet.hasNext()) {
			// user not exists. send 401
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		// compare token
		UserModel userModel = userSet.next();
		String code = userModel.getSecureCode();
		String token = info.serviceToken + "::" + info.clientToken;
		SecretKeySpec secureKey = new SecretKeySpec(code.getBytes(), "HmacMD5");
		try {
			Cipher cipher = Cipher.getInstance("HmacMD5");
			cipher.init(Cipher.ENCRYPT_MODE, secureKey);
			String tokenHash = new String(cipher.doFinal(token.getBytes()));
			if (logger.isDebugEnabled())
				logger.debug("[getUserModel] tokenHash = " + tokenHash);
			if (!auth.equals(tokenHash)) {
				// auth failed. send 401
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return null;
			}
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			logger.error("[getUserModel] HmacMD5 encrypt failed", e);
			throw new RuntimeException(e);
		}

		return userModel;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void runService(Service service, UserModel userModel,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (logger.isTraceEnabled())
			logger.trace("[runService] called");
		// get input parameter
		Type inputType = null;
		for (Type type : service.getClass().getGenericInterfaces()) {
			ParameterizedType ptype = (ParameterizedType) type;
			if (ptype.getRawType() == Service.class) {
				inputType = ptype.getActualTypeArguments()[0];
				break;
			}
		}
		// run service
		Gson gson = Tutor.getBeanFactory().getBean(Gson.class);
		Object input = gson.fromJson(request.getReader(), inputType);
		Object result;
		try {
			result = service.run(userModel, input);
		} catch (RuntimeException | ServletException | IOException | Error e) {
			logger.error("[runService] error occur", e);
			throw e;
		} catch (Throwable t) {
			logger.error("[runService] error occur", t);
			throw new ServletException(t);
		}
		// output result
		Writer writer = response.getWriter();
		gson.toJson(result, writer);
		writer.close();
	}

	protected String getServiceName(String path) {
		if (StringUtils.isEmpty(path))
			return null;
		return path.charAt(0) == '/' ? path.substring(1) : path;
	}

	static class AuthInfo {

		final String username;
		final String serviceName;
		final String serviceToken;
		final String clientToken;
		final long expiration;

		public AuthInfo(String username, String serviceName,
				String serviceToken, String clientToken, long expiration) {
			this.username = username;
			this.serviceName = serviceName;
			this.serviceToken = serviceToken;
			this.clientToken = clientToken;
			this.expiration = expiration;
		}

		@Override
		public String toString() {
			return new StringBuilder("AuthInfo {usrname = ").append(username)
					.append(", serviceName=").append(serviceName)
					.append(", serviceToken = ").append(serviceToken)
					.append(", clientToken = ").append(clientToken)
					.append(", expiration = ")
					.append(Tutor.formatDateTime(new Date(expiration)))
					.append("}").toString();
		}

	}

}
