/*
 * File   RegisterView.java
 * Create 2012/12/28
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.front;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.UserModel;
import com.changev.tutor.model.UserRole;
import com.changev.tutor.util.NamedLock;
import com.changev.tutor.util.Validator;
import com.changev.tutor.web.Messages;
import com.changev.tutor.web.View;
import com.db4o.ObjectContainer;

/**
 * <p>
 * 用户注册。
 * </p>
 * 
 * @author ren
 * 
 */
public class RegisterView implements View {

	private static final Logger logger = Logger.getLogger(RegisterView.class);

	private String successPage;

	NamedLock registerLock = new NamedLock();

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
		if (StringUtils.isNotEmpty(request.getParameter("register")))
			return register(request, response);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[postRender] called");
	}

	protected boolean register(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[register] called");

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		if (logger.isDebugEnabled()) {
			logger.debug("[register] username = " + username);
			logger.debug("[register] password = " + password);
			logger.debug("[register] confirmPassword = " + confirmPassword);
		}
		// validation
		if (Validator.required(username)) {
			Messages.addError(request, "username", "请输入登录用户名");
		} else if (Validator.minLength(username, 6)) {
			Messages.addError(request, "username", "登录用户名的长度必须大于6个字符");
		} else if (Validator.maxLength(username, 10)) {
			Messages.addError(request, "username", "登录用户名的长度必须小于10个字符");
		} else if (Validator.id(username)) {
			Messages.addError(request, "username", "登录用户名只能由汉字、英文字母、数字和下划线组成");
		}
		if (Validator.required(password)) {
			Messages.addError(request, "password", "请输入登录密码");
		} else if (Validator.minLength(password, 6)) {
			Messages.addError(request, "password", "登录密码的长度必须大于6个字符");
		} else if (Validator.maxLength(password, 10)) {
			Messages.addError(request, "password", "登录密码的长度必须小于10个字符");
		} else if (!password.equals(confirmPassword)) {
			Messages.addError(request, "password", "输入的密码不一致");
		}
		// save user
		if (!Messages.hasErrors(request)) {
			if (logger.isDebugEnabled())
				logger.debug("[register] validation passed");

			ObjectContainer objc = Tutor.getCurrentContainer();
			UserModel user = new UserModel();
			user.setUsername(username);

			registerLock.lock(username);
			try {
				// check the same username
				if (objc.queryByExample(user).isEmpty()) {
					if (logger.isDebugEnabled())
						logger.debug("[register] save user");

					try {
						// TODO encrypt password
						user.setPassword(password);
						user.setRole(UserRole.Student);
						user.setCreateDateTime(new Date());
						user.setUpdateDateTime(user.getCreateDateTime());
						user.setDeleted(Boolean.FALSE);
						objc.store(user);
						objc.commit();
						// goto success page
						if (logger.isDebugEnabled())
							logger.debug("[register] register successed. goto "
									+ successPage);
						response.sendRedirect(request.getContextPath()
								+ successPage);
						return false;
					} catch (Throwable t) {
						objc.rollback();
						throw t;
					}
				}
				Messages.addError(request, "username", "用户名已存在");
			} finally {
				registerLock.release(username);
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
