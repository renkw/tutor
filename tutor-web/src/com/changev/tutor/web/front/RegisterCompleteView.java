/*
 * File   registerCompleteView.java
 * Create 2013/01/14
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.front;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.ModelFactory;
import com.changev.tutor.model.StudentModel;
import com.changev.tutor.model.UserContactModel;
import com.changev.tutor.model.UserModel;
import com.changev.tutor.model.UserPrivacy;
import com.changev.tutor.web.Messages;
import com.changev.tutor.web.SessionContainer;
import com.changev.tutor.web.View;
import com.changev.tutor.web.util.ParamValidator;
import com.db4o.ObjectContainer;

/**
 * <p>
 * 用户注册。
 * </p>
 * 
 * @author ren
 * 
 */
public class RegisterCompleteView implements View {

	private static final Logger logger = Logger
			.getLogger(RegisterCompleteView.class);

	private ParamValidator registerValidator;
	private String userLockedMessage;
	private String successPage;

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
		if (StringUtils.isNotEmpty(request.getParameter("registerComplete")))
			return registerComplete(request, response);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[postRender] called");
	}

	protected boolean registerComplete(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[registerComplete] called");
		// get parameters
		String name = request.getParameter("name");
		String postcode = request.getParameter("postcode");
		String address1 = request.getParameter("address1");
		String address2 = request.getParameter("address2");
		String telephone = request.getParameter("telephone");
		String cellphone = request.getParameter("cellphone");
		String qq = request.getParameter("qq");
		String weibo = request.getParameter("weibo");
		String mailAddress = request.getParameter("mailAddress");
		String accountPrivacy = request.getParameter("accountPrivacy");
		String contactPrivacy = request.getParameter("contactPrivacy");
		if (logger.isDebugEnabled()) {
			logger.debug("[registerComplete] name = " + name);
			logger.debug("[registerComplete] postcode = " + postcode);
			logger.debug("[registerComplete] address1 = " + address1);
			logger.debug("[registerComplete] address2 = " + address2);
			logger.debug("[registerComplete] telephone = " + telephone);
			logger.debug("[registerComplete] cellphone = " + cellphone);
			logger.debug("[registerComplete] qq = " + qq);
			logger.debug("[registerComplete] weibo = " + weibo);
			logger.debug("[registerComplete] mailAddress = " + mailAddress);
			logger.debug("[registerComplete] accountPrivacy = "
					+ accountPrivacy);
			logger.debug("[registerComplete] contactPrivacy = "
					+ contactPrivacy);
		}
		// validation
		if (registerValidator == null || registerValidator.validate(request)) {
			ObjectContainer objc = Tutor.getCurrentContainer();
			UserModel parentModel = SessionContainer.getLoginUser(request);
			StudentModel studentModel = Tutor.one(objc
					.queryByExample(ModelFactory
							.getParentStudentExample(parentModel.getEmail())));
			if (studentModel == null) {
				logger.error("[registerComplete] student not found for "
						+ parentModel.getEmail());
				// TODO
				return true;
			}

			String lock = ModelFactory.getUserSemaphore(parentModel.getEmail());
			if (objc.ext().setSemaphore(lock, 0)) {
				try {
					UserContactModel contactModel = parentModel.getContact();
					if (contactModel == null) {
						contactModel = new UserContactModel();
						parentModel.setContact(contactModel);
					}

					contactModel.setName(name);
					contactModel.setPostcode(postcode);
					contactModel.setAddress1(address1);
					contactModel.setAddress2(address2);
					contactModel.setTelephone(telephone);
					contactModel.setCellphone(cellphone);
					contactModel.setQQ(qq);
					contactModel.setWeibo(weibo);
					contactModel.setMailAddress(mailAddress);

					UserPrivacy acc = UserPrivacy.valueOf(accountPrivacy);
					UserPrivacy con = UserPrivacy.valueOf(contactPrivacy);
					parentModel.setAccountPrivacy(acc);
					studentModel.setAccountPrivacy(acc);
					parentModel.setContactPrivacy(con);
					studentModel.setContactPrivacy(con);

					objc.store(studentModel);
					objc.commit();
					if (logger.isDebugEnabled())
						logger.debug("[registerComplete] success. goto "
								+ successPage);
					response.sendRedirect(request.getContextPath()
							+ successPage);
					return false;
				} catch (Throwable t) {
					logger.error("[registerComplete] failed", t);
					objc.rollback();
					throw t;
				} finally {
					objc.ext().releaseSemaphore(lock);
				}
			}
			Messages.addError(request, "name", userLockedMessage);
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
	 * @return the userLockedMessage
	 */
	public String getUserLockedMessage() {
		return userLockedMessage;
	}

	/**
	 * @param userLockedMessage
	 *            the userLockedMessage to set
	 */
	public void setUserLockedMessage(String userLockedMessage) {
		this.userLockedMessage = userLockedMessage;
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
