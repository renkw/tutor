/*
 * File   ModifyMemberView.java
 * Create 2013/01/20
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.back;

import java.util.Arrays;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.ModelFactory;
import com.changev.tutor.model.OrganizationModel;
import com.changev.tutor.model.TeacherModel;
import com.changev.tutor.model.UserContactModel;
import com.changev.tutor.model.UserModel;
import com.changev.tutor.model.UserPrivacy;
import com.changev.tutor.model.UserRole;
import com.changev.tutor.model.UserState;
import com.changev.tutor.web.Messages;
import com.changev.tutor.web.SessionContainer;
import com.changev.tutor.web.View;
import com.changev.tutor.web.util.ParamUtils;
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
		SessionContainer.get(request).setActionMessage(null);
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
		if (userModel.getRole() == UserRole.Teacher) {
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
		String name = request.getParameter("name");
		String gender = request.getParameter("gender");
		String[] birthday = request.getParameterValues("birthday");
		String[] area = request.getParameterValues("area");
		String[] subjects = request.getParameterValues("subjects");
		String grade = request.getParameter("grade");
		String gradeLevelFrom = request.getParameter("gradeLevelFrom");
		String gradeLevelTo = request.getParameter("gradeLevelTo");
		String education = request.getParameter("education");
		String teachedYears = request.getParameter("teachedYears");
		String[] speciality = request.getParameterValues("speciality");
		String homepage = request.getParameter("homepage");
		String description = request.getParameter("description");
		// contact
		String contactName = request.getParameter("name");
		String postcode = request.getParameter("postcode");
		String[] address = request.getParameterValues("address");
		String telephone = request.getParameter("telephone");
		String fax = request.getParameter("fax");
		String cellphone = request.getParameter("cellphone");
		String qq = request.getParameter("qq");
		String weibo = request.getParameter("weibo");
		String mailAddress = request.getParameter("mailAddress");
		String[] pos = request.getParameterValues("pos");
		// privacy
		String accountPrivacy = request.getParameter("accountPrivacy");
		String contactPrivacy = request.getParameter("contactPrivacy");
		if (logger.isDebugEnabled()) {
			logger.debug("[submit] name = " + name);
			logger.debug("[submit] gender = " + gender);
			logger.debug("[submit] birthday = " + Arrays.toString(birthday));
			logger.debug("[submit] area = " + Arrays.toString(area));
			logger.debug("[submit] subjects = " + Arrays.toString(subjects));
			logger.debug("[submit] grade = " + grade);
			logger.debug("[submit] gradeLevelFrom = " + gradeLevelFrom);
			logger.debug("[submit] gradeLevelTo = " + gradeLevelTo);
			logger.debug("[submit] education = " + education);
			logger.debug("[submit] teachedYears = " + teachedYears);
			logger.debug("[submit] speciality = " + Arrays.toString(speciality));
			logger.debug("[submit] homepage = " + homepage);
			logger.debug("[submit] description = " + description);
			logger.debug("[submit] contactName = " + contactName);
			logger.debug("[submit] postcode = " + postcode);
			logger.debug("[submit] address = " + Arrays.toString(address));
			logger.debug("[submit] telephone = " + telephone);
			logger.debug("[submit] fax = " + fax);
			logger.debug("[submit] cellphone = " + cellphone);
			logger.debug("[submit] qq = " + qq);
			logger.debug("[submit] weibo = " + weibo);
			logger.debug("[submit] mailAddress = " + mailAddress);
			logger.debug("[submit] pos = " + Arrays.toString(pos));
			logger.debug("[submit] accountPrivacy = " + accountPrivacy);
			logger.debug("[submit] contactPrivacy = " + contactPrivacy);
		}

		// validation
		if (submitValidator == null || submitValidator.validate(request)) {
			if (logger.isDebugEnabled())
				logger.debug("[submit] validation passed");

			ObjectContainer objc = Tutor.getCurrentContainer();

			UserModel loginUser = SessionContainer.getLoginUser(request);
			String lock = ModelFactory.getUserSemaphore(loginUser.getEmail());
			if (objc.ext().setSemaphore(lock, 0)) {
				try {
					loginUser.setName(ParamUtils.emptyNull(name));
					if (loginUser instanceof OrganizationModel) {
						// organization
						OrganizationModel orgModel = (OrganizationModel) loginUser;
						orgModel.setProvince(ParamUtils.emptyNull(area, 0));
						orgModel.setCity(ParamUtils.emptyNull(area, 1));
						orgModel.setDistrict(ParamUtils.emptyNull(area, 2));
						orgModel.getSubjectsFor().clear();
						if (subjects != null) {
							for (String s : subjects) {
								if (StringUtils.isEmpty(s))
									continue;
								orgModel.getSubjectsFor().add(s);
							}
						}
						orgModel.setHomepage(ParamUtils.emptyNull(homepage));
					} else if (loginUser instanceof TeacherModel) {
						// teacher
						TeacherModel teacherModel = (TeacherModel) loginUser;
						teacherModel.setMale("Male".equals(gender));
						teacherModel.setBirthday(StringUtils
								.join(birthday, '-'));
						teacherModel.setGrade(ParamUtils.emptyNull(grade));
						teacherModel.setGradeLevelFrom(ParamUtils
								.byteNull(gradeLevelFrom));
						teacherModel.setGradeLevelTo(ParamUtils
								.byteNull(gradeLevelTo));
						teacherModel.setEducation(ParamUtils
								.emptyNull(education));
						teacherModel.setTeachedYears(ParamUtils
								.byteNull(teachedYears));
						teacherModel.getSubjectsFor().clear();
						if (subjects != null) {
							for (String s : subjects) {
								if (StringUtils.isEmpty(s))
									continue;
								teacherModel.getSubjectsFor().add(s);
							}
						}
						teacherModel.getSpecialityFor().clear();
						if (speciality != null) {
							for (String s : speciality) {
								if (StringUtils.isEmpty(s))
									continue;
								teacherModel.getSpecialityFor().add(s);
							}
						}
						teacherModel
								.setHomepage(ParamUtils.emptyNull(homepage));
					}
					loginUser.setDescription(ParamUtils.emptyNull(description));
					loginUser.setAccountPrivacy(ParamUtils.enumNull(
							UserPrivacy.class, accountPrivacy));
					loginUser.setContactPrivacy(ParamUtils.enumNull(
							UserPrivacy.class, contactPrivacy));
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
					contactModel.setFax(ParamUtils.emptyNull(fax));
					contactModel.setCellphone(ParamUtils.emptyNull(cellphone));
					contactModel.setQQ(ParamUtils.emptyNull(qq));
					contactModel.setWeibo(ParamUtils.emptyNull(weibo));
					contactModel.setMailAddress(ParamUtils
							.emptyNull(mailAddress));
					contactModel.setPosX(ParamUtils.doubleNull(pos, 0));
					contactModel.setPosY(ParamUtils.doubleNull(pos, 1));
					// save
					objc.store(loginUser);
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
