/*
 * File   LoginView.java
 * Create 2012/12/28
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.front;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
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

	private String successPage;

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

		// TODO validate code
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (logger.isDebugEnabled()) {
			logger.debug("[login] username = " + username);
			logger.debug("[login] password = " + password);
		}
		// validation
		if (Validator.required(username)) {
			Messages.addError(request, "username", "请输入用户名");
		}
		if (Validator.required(password)) {
			Messages.addError(request, "password", "请输入密码");
		}
		// do login
		if (!Messages.hasErrors(request)) {
			if (logger.isDebugEnabled())
				logger.debug("[login] validation passed");

			ObjectContainer objc = Tutor.getCurrentContainer();
			UserModel user = new UserModel();
			user.setUsername(username);
			// TODO encrypt password
			user.setPassword(password);
			user.setDeleted(Boolean.FALSE);
			ObjectSet<UserModel> userSet = objc.queryByExample(user);
			if (!userSet.isEmpty()) {
				// reset session
				HttpSession session = request.getSession(false);
				if (session != null)
					session.invalidate();
				user = userSet.next();
				SessionContainer.get(request, true).setLoginUser(user);
				// TODO
				if (logger.isDebugEnabled())
					logger.debug("[login] login successed. goto " + successPage);
				response.sendRedirect(request.getContextPath() + successPage);
			}
		}
		return true;
	}

	/**
	 * @return the successPage
	 */
	public String getSuccessPage() {
		return successPage;
	}

	/**
	 * @param successPage
	 *            the successPage to set
	 */
	public void setSuccessPage(String successPage) {
		this.successPage = successPage;
	}

}
