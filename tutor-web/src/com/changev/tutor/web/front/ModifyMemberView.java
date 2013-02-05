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
import com.changev.tutor.model.ParentModel;
import com.changev.tutor.model.StudentModel;
import com.changev.tutor.model.UserContactModel;
import com.changev.tutor.model.UserModel;
import com.changev.tutor.model.UserPrivacy;
import com.changev.tutor.model.UserState;
import com.changev.tutor.web.Messages;
import com.changev.tutor.web.SessionContainer;
import com.changev.tutor.web.View;
import com.changev.tutor.web.util.ParamUtils;
import com.changev.tutor.web.util.ParamValidator;
import com.db4o.ObjectContainer;

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
		setVariables(request, null, null);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
		SessionContainer.get(request).setActionMessage(null);
	}

	protected void setVariables(HttpServletRequest request, ParentModel parent,
			StudentModel student) {
		if (logger.isTraceEnabled())
			logger.trace("[setVariables] called");

		// the user
		UserModel userModel = SessionContainer.getLoginUser(request);
		if (student == null) {
			if (userModel instanceof StudentModel) {
				student = (StudentModel) userModel;
			} else if (userModel instanceof ParentModel) {
				student = Tutor.one(((ParentModel) userModel).getChildren());
				if (student == null) {
					student = new StudentModel();
					student.setAccountPrivacy(UserPrivacy.ContacterOnly);
					student.setParent((ParentModel) userModel);
				}
			}
		}
		if (parent == null)
			parent = student.getParent();
		request.setAttribute("parent", parent);
		request.setAttribute("student", student);
		// contact
		UserContactModel contactModel = parent.getContact();
		if (contactModel == null)
			contactModel = new UserContactModel();
		request.setAttribute("contact", contactModel);
		// areas
		request.setAttribute("areas", Tutor.getConstant("areas"));
		// subjects
		request.setAttribute("subjects", Tutor.getConstant("subjects"));
		// birthYear
		request.setAttribute("birthYear",
				Tutor.currentCalendar().get(Calendar.YEAR) - 1);
		// grades
		request.setAttribute("grades", Tutor.getConstant("grades"));
		request.setAttribute("gradeLevels", Tutor.getConstant("gradeLevels"));
	}

	protected boolean submit(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[setVariables] called");

		// basic
		String name = request.getParameter("name");
		String studentName = request.getParameter("studentName");
		String gender = request.getParameter("gender");
		String[] birthday = request.getParameterValues("birthday");
		String[] area = request.getParameterValues("area");
		String school = request.getParameter("school");
		String grade = request.getParameter("grade");
		String gradeLevel = request.getParameter("gradeLevel");
		String hobby = request.getParameter("hobby");
		String description = request.getParameter("description");
		String[] subject = request.getParameterValues("subject");
		String[] answerer = request.getParameterValues("answerer");
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
		String studentPrivacy = request.getParameter("studentPrivacy");
		String contactPrivacy = request.getParameter("contactPrivacy");
		if (logger.isDebugEnabled()) {
			logger.debug("[submit] name = " + name);
			logger.debug("[submit] area = " + Arrays.toString(area));
			logger.debug("[submit] studentName = " + studentName);
			logger.debug("[submit] grade = " + grade);
			logger.debug("[submit] gradeLevel = " + gradeLevel);
			logger.debug("[submit] gender = " + gender);
			logger.debug("[submit] birthday = " + Arrays.toString(birthday));
			logger.debug("[submit] school = " + school);
			logger.debug("[submit] hobby = " + hobby);
			logger.debug("[submit] description = " + description);
			logger.debug("[submit] subject = " + Arrays.toString(subject));
			logger.debug("[submit] answerer = " + Arrays.toString(answerer));
			logger.debug("[submit] contactName = " + contactName);
			logger.debug("[submit] postcode = " + postcode);
			logger.debug("[submit] address = " + Arrays.toString(address));
			logger.debug("[submit] telephone = " + telephone);
			logger.debug("[submit] cellphone = " + cellphone);
			logger.debug("[submit] qq = " + qq);
			logger.debug("[submit] weibo = " + weibo);
			logger.debug("[submit] mailAddress = " + mailAddress);
			logger.debug("[submit] accountPrivacy = " + accountPrivacy);
			logger.debug("[submit] studentPrivacy = " + studentPrivacy);
			logger.debug("[submit] contactPrivacy = " + contactPrivacy);
		}

		// validation
		ParentModel parentModel = null;
		StudentModel studentModel = null;
		if (submitValidator == null || submitValidator.validate(request)) {
			if (logger.isDebugEnabled())
				logger.debug("[submit] validation passed");

			ObjectContainer objc = Tutor.getCurrentContainer();

			UserModel loginUser = SessionContainer.getLoginUser(request);
			String lock = ModelFactory.getUserSemaphore(loginUser.getEmail());
			if (objc.ext().setSemaphore(lock, 0)) {
				try {
					if (loginUser instanceof StudentModel) {
						studentModel = (StudentModel) loginUser;
					} else if (loginUser instanceof ParentModel) {
						studentModel = Tutor.one(((ParentModel) loginUser)
								.getChildren());
						if (studentModel == null) {
							studentModel = new StudentModel();
							studentModel.setParent((ParentModel) loginUser);
							studentModel.setState(UserState.Incomplete);
						}
					}
					parentModel = studentModel.getParent();

					// parent
					if (loginUser instanceof ParentModel) {
						parentModel.setProvince(ParamUtils.emptyNull(area, 0));
						parentModel.setCity(ParamUtils.emptyNull(area, 1));
						parentModel.setDistrict(ParamUtils.emptyNull(area, 2));
					}
					// student
					studentModel.setName(ParamUtils.emptyNull(studentName));
					studentModel.setMale("Male".equals(gender));
					studentModel.setBirthday(StringUtils.leftPad(birthday[0],
							4, '0')
							+ '-'
							+ StringUtils.leftPad(birthday[1], 2, '0')
							+ '-'
							+ StringUtils.leftPad(birthday[2], 2, '0'));
					studentModel.setSchool(ParamUtils.emptyNull(school));
					studentModel.setGrade(ParamUtils.emptyNull(grade));
					studentModel.setGradeLevel(ParamUtils.byteNull(gradeLevel));
					studentModel.setHobby(ParamUtils.emptyNull(hobby));
					studentModel.getDefaultAnswererFor().clear();
					if (subject != null && answerer != null) {
						for (int i = 0; i < subject.length
								&& i < answerer.length; i++) {
							if (StringUtils.isEmpty(subject[i])
									|| StringUtils.isEmpty(answerer[i]))
								continue;
							UserModel answerUser = Tutor.one(objc
									.queryByExample(ModelFactory
											.getUserExample(answerer[i])));
							if (answerUser == null)
								continue;
							studentModel.getDefaultAnswererFor().put(
									subject[i], answerUser);
							// studentModel.getContactersFor().add(teacher);
							// parentModel.getContactersFor().add(teacher);
						}
					}
					studentModel.setDescription(ParamUtils
							.emptyNull(description));
					studentModel.setAccountPrivacy(ParamUtils.enumNull(
							UserPrivacy.class, studentPrivacy));
					// user
					loginUser.setName(ParamUtils.emptyNull(name));
					loginUser.setAccountPrivacy(ParamUtils.enumNull(
							UserPrivacy.class, accountPrivacy));
					loginUser.setContactPrivacy(ParamUtils.enumNull(
							UserPrivacy.class, contactPrivacy));
					if (loginUser.getState() == UserState.Incomplete)
						loginUser.setState(UserState.Activated);
					// contact
					UserContactModel contactModel = loginUser.getContact();
					if (contactModel == null) {
						contactModel = new UserContactModel();
						loginUser.setContact(contactModel);
					}
					contactModel.setName(ParamUtils.emptyNull(contactName));
					contactModel.setPostcode(ParamUtils.emptyNull(postcode));
					contactModel.setAddress1(ParamUtils.emptyNull(address, 0));
					contactModel.setAddress2(ParamUtils.emptyNull(address, 1));
					contactModel.setTelephone(ParamUtils.emptyNull(telephone));
					contactModel.setCellphone(ParamUtils.emptyNull(cellphone));
					contactModel.setQQ(ParamUtils.emptyNull(qq));
					contactModel.setWeibo(ParamUtils.emptyNull(weibo));
					contactModel.setMailAddress(ParamUtils
							.emptyNull(mailAddress));
					// save
					objc.store(studentModel);
					objc.commit();
					SessionContainer.get(request).setActionMessage("修改用户资料成功。");
					if (StringUtils.isNotEmpty(successPage)) {
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
					objc.ext().releaseSemaphore(lock);
				}
			} else {
				Messages.addError(request, "email", userLockedMessage);
			}
		}

		setVariables(request, parentModel, studentModel);
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
