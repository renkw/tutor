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
import com.changev.tutor.model.OrganizationModel;
import com.changev.tutor.model.ParentModel;
import com.changev.tutor.model.UserModel;
import com.changev.tutor.model.UserState;
import com.changev.tutor.web.Messages;
import com.changev.tutor.web.SessionContainer;
import com.changev.tutor.web.View;
import com.changev.tutor.web.util.ParamUtils;
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
	private String userSuccessPage;
	private String orgSuccessPage;

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
		if (StringUtils.isNotEmpty(request.getParameter("registerUser")))
			return registerUser(request, response);
		if (StringUtils.isNotEmpty(request.getParameter("registerOrg")))
			return registerOrg(request, response);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[postRender] called");
	}

	protected boolean registerUser(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[registerUser] called");

		ParentModel parentModel = new ParentModel();
		parentModel.setState(UserState.Incomplete);
		if (register(request, response, parentModel))
			return true;

		if (logger.isDebugEnabled())
			logger.debug("[registerUser] success. goto " + userSuccessPage);
		response.sendRedirect(request.getContextPath() + userSuccessPage);
		return false;
	}

	protected boolean registerOrg(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[registerOrg] called");

		OrganizationModel parentModel = new OrganizationModel();
		parentModel.setState(UserState.Incomplete);
		if (register(request, response, parentModel))
			return true;

		if (logger.isDebugEnabled())
			logger.debug("[registerOrg] success. goto " + orgSuccessPage);
		response.sendRedirect(request.getContextPath() + orgSuccessPage);
		return false;
	}

	protected boolean register(HttpServletRequest request,
			HttpServletResponse response, UserModel userModel) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[register] called");
		// get parameters
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		String checkCode = request.getParameter("checkCode");
		if (logger.isDebugEnabled()) {
			logger.debug("[register] name = " + name);
			logger.debug("[register] email = " + email);
			logger.debug("[register] password = " + password);
			logger.debug("[register] confirmPassword = " + confirmPassword);
			logger.debug("[register] checkCode = " + checkCode);
		}
		// validation
		if (registerValidator == null || registerValidator.validate(request)) {
			if (logger.isDebugEnabled())
				logger.debug("[register] validation passed");

			ObjectContainer objc = Tutor.getCurrentContainer();
			String lock = ModelFactory.getUserSemaphore(email);
			if (objc.ext().setSemaphore(lock, 0)) {
				try {
					ObjectSet<UserModel> userSet = objc
							.queryByExample(ModelFactory.getUserExample(email));
					if (!userSet.hasNext()) {
						// register
						userModel.setName(ParamUtils.emptyNull(name));
						userModel.setEmail(ParamUtils.emptyNull(email));
						userModel.setPassword(ModelFactory
								.encryptPassword(password));
						objc.store(userModel);
						objc.commit();
						// login
						request.getSession().invalidate();
						SessionContainer.get(request).login(
								objc.ext().getID(userModel));
						return false;
					}
				} catch (Throwable t) {
					logger.error("[register] failed", t);
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
	 * @return the userSuccessPage
	 */
	public String getUserSuccessPage() {
		return userSuccessPage;
	}

	/**
	 * @param userSuccessPage
	 *            the userSuccessPage to set
	 */
	public void setUserSuccessPage(String userSuccessPage) {
		this.userSuccessPage = userSuccessPage;
	}

	/**
	 * @return the orgSuccessPage
	 */
	public String getOrgSuccessPage() {
		return orgSuccessPage;
	}

	/**
	 * @param orgSuccessPage
	 *            the orgSuccessPage to set
	 */
	public void setOrgSuccessPage(String orgSuccessPage) {
		this.orgSuccessPage = orgSuccessPage;
	}

}
