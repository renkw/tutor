/*
 * File   LoginView.java
 * Create 2012/12/28
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.pub;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.ModelFactory;
import com.changev.tutor.model.UserModel;
import com.changev.tutor.web.Messages;
import com.changev.tutor.web.SessionContainer;
import com.changev.tutor.web.View;
import com.changev.tutor.web.util.ParamValidator;
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

	private ParamValidator loginValidator;
	private String failMessage;
	private Map<String, String> successPages;

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
		if (StringUtils.isNotEmpty(request.getParameter("login")))
			return login(request, response);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
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
			HttpServletResponse response) throws Throwable {
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
		if (loginValidator == null || loginValidator.validate(request)) {
			if (logger.isDebugEnabled())
				logger.debug("[login] validation passed");

			ObjectContainer objc = Tutor.getCurrentContainer();
			ObjectSet<UserModel> userSet = objc.queryByExample(ModelFactory
					.getUserExample(email,
							ModelFactory.encryptPassword(password)));
			if (userSet.hasNext()) {
				// reset session
				request.getSession().invalidate();
				UserModel user = userSet.next();
				SessionContainer.get(request, true).login(
						objc.ext().getID(user));
				String pageKey = user.getRole().name() + user.getState().name();
				String successPage = successPages.get(pageKey);
				if (StringUtils.isEmpty(successPage))
					successPage = successPages.get(user.getRole().name());
				if (StringUtils.isEmpty(successPage)) {
					logger.error("[login] no avaliable entry page for "
							+ pageKey);
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
				} else {
					if (logger.isDebugEnabled())
						logger.debug("[login] login successed. goto "
								+ successPage);
					response.sendRedirect(request.getContextPath()
							+ successPage);
				}
				return false;
			}
			Messages.addError(request, "email", failMessage);
		}
		return true;
	}

	/**
	 * @return the loginValidator
	 */
	public ParamValidator getLoginValidator() {
		return loginValidator;
	}

	/**
	 * @param loginValidator
	 *            the loginValidator to set
	 */
	public void setLoginValidator(ParamValidator loginValidator) {
		this.loginValidator = loginValidator;
	}

	/**
	 * @return the failMessage
	 */
	public String getFailMessage() {
		return failMessage;
	}

	/**
	 * @param failMessage
	 *            the failMessage to set
	 */
	public void setFailMessage(String failMessage) {
		this.failMessage = failMessage;
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
