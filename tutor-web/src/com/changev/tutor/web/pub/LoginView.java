/*
 * File   LoginView.java
 * Create 2012/12/28
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.pub;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.model.ModelFactory;
import com.changev.tutor.model.UserModel;
import com.changev.tutor.util.Validator;
import com.changev.tutor.web.Messages;
import com.changev.tutor.web.SessionContainer;
import com.changev.tutor.web.View;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

/**
 * <p>
 * 用户登录。
 * </p>
 * 
 * @author ren
 * 
 */
public class LoginView implements View {

	private static final Logger logger = Logger.getLogger(LoginView.class);

	private Map<String, String> successPages;

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer objc)
			throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
		if (StringUtils.isNotEmpty(request.getParameter("login")))
			return login(request, response, objc);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer objc)
			throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[postRender] called");
	}

	/**
	 * <p>
	 * 执行用户登录。
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Throwable
	 */
	protected boolean login(HttpServletRequest request,
			HttpServletResponse response, ObjectContainer objc)
			throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[login] called");

		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String checkCode = request.getParameter("checkCode");
		if (logger.isDebugEnabled()) {
			logger.debug("[login] email = " + email);
			logger.debug("[login] password = " + password);
			logger.debug("[login] checkCode = " + checkCode);
		}
		// validation
		if (Validator.required(email))
			Messages.addError(request, "email", "请输入登录邮箱");
		if (Validator.required(password))
			Messages.addError(request, "password", "请输入密码");
		SessionContainer container = SessionContainer.get(request);
		if (container == null
				|| !StringUtils.equals(container.getCheckCode(), checkCode))
			Messages.addError(request, "checkCode", "验证码错误");
		// do login
		if (!Messages.hasErrors(request)) {
			if (logger.isDebugEnabled())
				logger.debug("[login] validation passed");
			ObjectSet<UserModel> userSet = objc.queryByExample(ModelFactory
					.getUserExample(email, password));
			if (userSet.hasNext()) {
				// reset session
				HttpSession session = request.getSession(false);
				if (session != null)
					session.invalidate();
				UserModel user = userSet.next();
				SessionContainer.get(request, true).setLoginUser(
						objc.ext().getID(user), user);
				String successPage = successPages.get(user.getRole().name());
				if (logger.isDebugEnabled())
					logger.debug("[login] login successed. goto " + successPage);
				response.sendRedirect(request.getContextPath() + successPage);
				return false;
			}
			Messages.addError(request, "email", "用户不存在或密码错误");
		}
		return true;
	}

	/**
	 * @return the successPages
	 */
	public Map<String, String> getSuccessPages() {
		return successPages;
	}

	/**
	 * @param successPages
	 *            the successPages to set
	 */
	public void setSuccessPages(Map<String, String> successPages) {
		this.successPages = successPages;
	}

}
