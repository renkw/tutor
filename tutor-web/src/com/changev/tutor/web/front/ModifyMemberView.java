/*
 * File   ModifyMemberView.java
 * Create 2013/01/20
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.front;

import java.util.Arrays;
import java.util.Calendar;

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
import com.changev.tutor.model.UserRole;
import com.changev.tutor.model.UserState;
import com.changev.tutor.web.Messages;
import com.changev.tutor.web.SessionContainer;
import com.changev.tutor.web.View;
import com.changev.tutor.web.util.ParamValidator;
import com.db4o.ObjectContainer;
import com.google.gson.Gson;

/**
 * <p>
 * 修改机构/教师资料。
 * </p>
 * 
 * @author ren
 * 
 */
public class ModifyMemberView implements View {

	private static final Logger logger = Logger
			.getLogger(ModifyMemberView.class);

	private ParamValidator submitValidator;
	private String duplicatedEmailMessage;
	private String userLockedMessage;
	private String successPage;

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
		if (StringUtils.isNotEmpty(request.getParameter("submit")))
			return submit(request, response);
		setVariables(request);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
	}

	protected void setVariables(HttpServletRequest request) {
		if (logger.isTraceEnabled())
			logger.trace("[setVariables] called");

		String sId = request.getParameter("id");
		if (logger.isDebugEnabled())
			logger.debug("[setVariables] id = " + sId);

		Gson gson = Tutor.getBeanFactory().getBean(Gson.class);
		// the user
		UserModel userModel = SessionContainer.getLoginUser(request);
		request.setAttribute("user", userModel);
		// contact
		UserContactModel contactModel = userModel.getContact();
		if (contactModel == null)
			contactModel = new UserContactModel();
		request.setAttribute("contact", contactModel);
		// areas
		request.setAttribute("areas", Tutor.getConstant("areas"));
		request.setAttribute("areaJson",
				gson.toJson(Tutor.getConstant("areas")));
		// subjects
		request.setAttribute("subjects", Tutor.getConstant("subjects"));
		if (userModel.getRole() == UserRole.Student) {
			// birthYear
			request.setAttribute("birthYear",
					Tutor.currentCalendar().get(Calendar.YEAR) - 1);
			// grades
			request.setAttribute("grades", Tutor.getConstant("grades"));
			request.setAttribute("gradeLevelJson",
					gson.toJson(Tutor.getConstant("gradeLevels")));
			// specility
			request.setAttribute("speciality", Tutor.getConstant("speciality"));
		}
	}

	protected boolean submit(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[setVariables] called");

		// basic
		String email = request.getParameter("email");
		String name = request.getParameter("name");
		String gender = request.getParameter("gender");
		String[] birthday = request.getParameterValues("birthday");
		String[] area = request.getParameterValues("area");
		String school = request.getParameter("school");
		String subject = request.getParameter("subject");
		String grade = request.getParameter("grade");
		String gradeLevel = request.getParameter("gradeLevel");
		String hobby = request.getParameter("hobby");
		String description = request.getParameter("description");
		// contact
		String contactName = request.getParameter("name");
		String postcode = request.getParameter("postcode");
		String[] address = request.getParameterValues("address");
		String telephone = request.getParameter("telephone");
		String cellphone = request.getParameter("cellphone");
		String qq = request.getParameter("qq");
		String weibo = request.getParameter("weibo");
		String mailAddress = request.getParameter("mailAddress");
		// privacy
		String accountPrivacy = request.getParameter("accountPrivacy");
		String contactPrivacy = request.getParameter("contactPrivacy");
		if (logger.isDebugEnabled()) {
			logger.debug("[submit] email = " + email);
			logger.debug("[submit] name = " + name);
			logger.debug("[submit] gender = " + gender);
			logger.debug("[submit] birthday = " + Arrays.toString(birthday));
			logger.debug("[submit] area = " + Arrays.toString(area));
			logger.debug("[submit] school = " + school);
			logger.debug("[submit] subject = " + subject);
			logger.debug("[submit] grade = " + grade);
			logger.debug("[submit] gradeLevel = " + gradeLevel);
			logger.debug("[submit] hobby = " + hobby);
			logger.debug("[submit] description = " + description);
			logger.debug("[submit] contactName = " + contactName);
			logger.debug("[submit] postcode = " + postcode);
			logger.debug("[submit] address = " + Arrays.toString(address));
			logger.debug("[submit] telephone = " + telephone);
			logger.debug("[submit] cellphone = " + cellphone);
			logger.debug("[submit] qq = " + qq);
			logger.debug("[submit] weibo = " + weibo);
			logger.debug("[submit] mailAddress = " + mailAddress);
			logger.debug("[submit] accountPrivacy = " + accountPrivacy);
			logger.debug("[submit] contactPrivacy = " + contactPrivacy);
		}

		// validation
		if (submitValidator == null || submitValidator.validate(request)) {
			if (logger.isDebugEnabled())
				logger.debug("[submit] validation passed");

			ObjectContainer objc = Tutor.getCurrentContainer();

			UserModel loginUser = SessionContainer.getLoginUser(request);
			boolean emailChanged = !StringUtils.equals(email,
					loginUser.getEmail());
			String lock = ModelFactory.getUserSemaphore(loginUser.getEmail());
			String newLock = ModelFactory.getUserSemaphore(email);
			if (objc.ext().setSemaphore(lock, 0)) {
				try {
					if (emailChanged
							&& (!objc.ext().setSemaphore(newLock, 0) || objc
									.queryByExample(
											ModelFactory.getUserExample(email))
									.hasNext())) {
						Messages.addError(request, "email",
								duplicatedEmailMessage);
					} else {
						loginUser.setEmail(Tutor.emptyNull(email));
						loginUser.setName(Tutor.emptyNull(name));
						loginUser.setProvince(area == null ? null : Tutor
								.emptyNull(area[0]));
						loginUser.setCity(area == null ? null : Tutor
								.emptyNull(area[1]));
						loginUser.setDistrict(area == null ? null : Tutor
								.emptyNull(area[2]));
						if (loginUser.getRole() == UserRole.Student) {
							// teacher
							StudentModel studentModel = loginUser
									.as(UserRole.Student);
							studentModel.setMale("Male".equals(gender));
							studentModel.setBirthday(StringUtils.join(birthday,
									'-'));
							studentModel.setSchool(Tutor.emptyNull(school));
							studentModel.setGrade(Tutor.emptyNull(grade));
							studentModel.setGradeLevel(Tutor
									.byteNull(gradeLevel));
							studentModel.setHobby(Tutor.emptyNull(hobby));
						}
						loginUser.setDescription(Tutor.emptyNull(description));
						loginUser.setAccountPrivacy(Tutor.enumNull(
								UserPrivacy.class, accountPrivacy));
						loginUser.setContactPrivacy(Tutor.enumNull(
								UserPrivacy.class, contactPrivacy));
						loginUser.setState(UserState.Activated);
						// contact
						UserContactModel contactModel = loginUser.getContact();
						if (contactModel == null) {
							contactModel = new UserContactModel();
							loginUser.setContact(contactModel);
						}
						contactModel.setName(Tutor.emptyNull(contactName));
						contactModel.setPostcode(Tutor.emptyNull(postcode));
						contactModel.setAddress1(address == null ? null : Tutor
								.emptyNull(address[0]));
						contactModel.setAddress2(address == null ? null : Tutor
								.emptyNull(address[1]));
						contactModel.setTelephone(Tutor.emptyNull(telephone));
						contactModel.setCellphone(Tutor.emptyNull(cellphone));
						contactModel.setQQ(Tutor.emptyNull(qq));
						contactModel.setWeibo(Tutor.emptyNull(weibo));
						contactModel.setMailAddress(Tutor
								.emptyNull(mailAddress));
						// save
						objc.store(loginUser);
						objc.commit();
						if (logger.isDebugEnabled())
							logger.debug("[submit] success. goto "
									+ successPage);
						response.sendRedirect(request.getContextPath()
								+ successPage);
						return false;
					}
				} catch (Throwable t) {
					logger.error("[submit] failed", t);
					objc.rollback();
					throw t;
				} finally {
					if (emailChanged)
						objc.ext().releaseSemaphore(newLock);
					objc.ext().releaseSemaphore(lock);
				}
			}
			Messages.addError(request, "email", userLockedMessage);
		}

		setVariables(request);
		return true;
	}

	/**
	 * @return the submitValidator
	 */
	public ParamValidator getSubmitValidator() {
		return submitValidator;
	}

	/**
	 * @param submitValidator
	 *            the submitValidator to set
	 */
	public void setSubmitValidator(ParamValidator submitValidator) {
		this.submitValidator = submitValidator;
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
