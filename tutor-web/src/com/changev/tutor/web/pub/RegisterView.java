/*
 * File   RegisterView.java
 * Create 2013/01/14
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.pub;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.ModelFactory;
import com.changev.tutor.model.ParentModel;
import com.changev.tutor.model.UserModel;
import com.changev.tutor.model.UserState;
import com.changev.tutor.web.Messages;
import com.changev.tutor.web.SessionContainer;
import com.changev.tutor.web.View;
import com.changev.tutor.web.util.ParamValidator;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

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

	private ParamValidator registerValidator;
	private String duplicatedEmailMessage;
	private String successPage;

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
		if (StringUtils.isNotEmpty(request.getParameter("registerAccount")))
			return registerAccount(request, response);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[postRender] called");
	}

	protected boolean registerAccount(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[registerAccount] called");
		// get parameters
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		String checkCode = request.getParameter("checkCode");
		if (logger.isDebugEnabled()) {
			logger.debug("[registerAccount] name = " + name);
			logger.debug("[registerAccount] email = " + email);
			logger.debug("[registerAccount] password = " + password);
			logger.debug("[registerAccount] confirmPassword = "
					+ confirmPassword);
			logger.debug("[registerAccount] checkCode = " + checkCode);
		}
		// validation
		if (registerValidator == null || registerValidator.validate(request)) {
			ObjectContainer objc = Tutor.getCurrentContainer();
			String lock = ModelFactory.getUserSemaphore(email);
			if (objc.ext().setSemaphore(lock, 0)) {
				try {
					ObjectSet<UserModel> userSet = objc
							.queryByExample(ModelFactory.getUserExample(email));
					if (!userSet.hasNext()) {
						// register
						ParentModel parentModel = new ParentModel();
						parentModel.setName(name);
						parentModel.setEmail(email);
						parentModel.setPassword(ModelFactory
								.encryptPassword(password));
						parentModel.setState(UserState.Incomplete);
						objc.store(parentModel);
						objc.commit();
						// login
						request.getSession().invalidate();
						SessionContainer.get(request).login(
								objc.ext().getID(parentModel));
						if (logger.isDebugEnabled())
							logger.debug("[registerAccount] success. goto "
									+ successPage);
						response.sendRedirect(request.getContextPath()
								+ successPage);
						return false;
					}
				} catch (Throwable t) {
					logger.error("[registerAccount] failed", t);
					objc.rollback();
					throw t;
				} finally {
					objc.ext().releaseSemaphore(lock);
				}
			}
			Messages.addError(request, "email", duplicatedEmailMessage);
		}
		return true;
	}

	/**
	 * @return the registerValidator
	 */
	public ParamValidator getRegisterValidator() {
		return registerValidator;
	}

	/**
	 * @param registerValidator
	 *            the registerValidator to set
	 */
	public void setRegisterValidator(ParamValidator registerValidator) {
		this.registerValidator = registerValidator;
	}

	/**
	 * @return the duplicatedEmailMessage
	 */
	public String getDuplicatedEmailMessage() {
		return duplicatedEmailMessage;
	}

	/**
	 * @param duplicatedEmailMessage
	 *            the duplicatedEmailMessage to set
	 */
	public void setDuplicatedEmailMessage(String duplicatedEmailMessage) {
		this.duplicatedEmailMessage = duplicatedEmailMessage;
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
