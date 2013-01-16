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
import com.changev.tutor.web.Messages;
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

	private ParamValidator registerAccountValidator;
	private String duplicatedEmailMessage;
	private String registerRequiredPage;

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
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		String checkCode = request.getParameter("checkCode");
		if (logger.isDebugEnabled()) {
			logger.debug("[registerAccount] email = " + email);
			logger.debug("[registerAccount] password = " + password);
			logger.debug("[registerAccount] confirmPassword = "
					+ confirmPassword);
			logger.debug("[registerAccount] checkCode = " + checkCode);
		}
		// validation
		if (registerAccountValidator == null
				|| registerAccountValidator.validate(request)) {
			ObjectContainer objc = Tutor.getCurrentContainer();
			String lock = ModelFactory.getUserSemaphore(email);
			if (objc.ext().setSemaphore(lock, 0)) {
				try {
					ObjectSet<UserModel> userSet = objc
							.queryByExample(ModelFactory.getUserExample(email));
					if (!userSet.hasNext()) {
						ParentModel parentModel = new ParentModel();
						parentModel.setEmail(email);
						parentModel.setPassword(ModelFactory
								.encryptPassword(password));
						objc.store(parentModel);
						objc.commit();
						if (logger.isDebugEnabled())
							logger.debug("[registerAccount] success. goto "
									+ registerRequiredPage);
						request.getRequestDispatcher(registerRequiredPage)
								.forward(request, response);
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

	protected boolean registerRequired(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
		return false;
	}

	protected boolean registerAdditional(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @return the registerAccountValidator
	 */
	public ParamValidator getRegisterAccountValidator() {
		return registerAccountValidator;
	}

	/**
	 * @param registerAccountValidator
	 *            the registerAccountValidator to set
	 */
	public void setRegisterAccountValidator(
			ParamValidator registerAccountValidator) {
		this.registerAccountValidator = registerAccountValidator;
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
	 * @return the registerRequiredPage
	 */
	public String getRegisterRequiredPage() {
		return registerRequiredPage;
	}

	/**
	 * @param registerRequiredPage
	 *            the registerRequiredPage to set
	 */
	public void setRegisterRequiredPage(String registerRequiredPage) {
		this.registerRequiredPage = registerRequiredPage;
	}

}
