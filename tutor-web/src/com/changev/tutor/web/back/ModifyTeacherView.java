/*
 * File   ModifyTeacherView.java
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
import com.changev.tutor.model.UserPrivacy;
import com.changev.tutor.model.UserState;
import com.changev.tutor.web.Messages;
import com.changev.tutor.web.SessionContainer;
import com.changev.tutor.web.View;
import com.changev.tutor.web.util.ParamValidator;
import com.db4o.ObjectContainer;
import com.google.gson.Gson;

/**
 * <p>
 * 修改/添加教师
 * </p>
 * 
 * @author ren
 * 
 */
public class ModifyTeacherView implements View {

	private static final Logger logger = Logger
			.getLogger(ModifyTeacherView.class);

	private ParamValidator submitValidator;
	private ParamValidator deleteValidator;
	private String duplicatedEmailMessage;
	private String userLockedMessage;
	private String submitSuccessPage;
	private String deleteSuccessPage;

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
		if (StringUtils.isNotEmpty(request.getParameter("submit")))
			return submit(request, response);
		if (StringUtils.isNotEmpty(request.getParameter("delete")))
			return delete(request, response);
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
		// the teacher
		TeacherModel teacherModel = StringUtils.isNotEmpty(sId) ? (TeacherModel) Tutor
				.getCurrentContainerExt().getByID(Long.parseLong(sId))
				: new TeacherModel();
		request.setAttribute("teacher", teacherModel);
		// contact
		UserContactModel contactModel = teacherModel.getContact();
		if (contactModel == null)
			contactModel = SessionContainer.getLoginUser(request).getContact();
		if (contactModel == null)
			contactModel = new UserContactModel();
		request.setAttribute("contact", contactModel);
		// birthYear
		request.setAttribute("birthYear",
				Tutor.currentCalendar().get(Calendar.YEAR) - 1);
		// areas
		request.setAttribute("areas", Tutor.getConstant("areas"));
		request.setAttribute("areaJson",
				gson.toJson(Tutor.getConstant("areas")));
		// grades
		request.setAttribute("grades", Tutor.getConstant("grades"));
		request.setAttribute("gradeLevelJson",
				gson.toJson(Tutor.getConstant("gradeLevels")));
		// subjects
		request.setAttribute("subjects", Tutor.getConstant("subjects"));
		// specility
		request.setAttribute("speciality", Tutor.getConstant("speciality"));
	}

	protected boolean submit(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[setVariables] called");

		// basic
		String id = request.getParameter("id");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String gender = request.getParameter("gender");
		String[] birthday = request.getParameterValues("birthday");
		String province = request.getParameter("province");
		String city = request.getParameter("city");
		String district = request.getParameter("district");
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
		String address1 = request.getParameter("address1");
		String address2 = request.getParameter("address2");
		String telephone = request.getParameter("telephone");
		String fax = request.getParameter("fax");
		String cellphone = request.getParameter("cellphone");
		String qq = request.getParameter("qq");
		String weibo = request.getParameter("weibo");
		String mailAddress = request.getParameter("mailAddress");
		// privacy
		String accountPrivacy = request.getParameter("accountPrivacy");
		String contactPrivacy = request.getParameter("contactPrivacy");
		if (logger.isDebugEnabled()) {
			logger.debug("[submit] id = " + id);
			logger.debug("[submit] email = " + email);
			logger.debug("[submit] password = " + password);
			logger.debug("[submit] name = " + name);
			logger.debug("[submit] gender = " + gender);
			logger.debug("[submit] birthday = " + Arrays.toString(birthday));
			logger.debug("[submit] province = " + province);
			logger.debug("[submit] city = " + city);
			logger.debug("[submit] district = " + district);
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
			logger.debug("[submit] address1 = " + address1);
			logger.debug("[submit] address2 = " + address2);
			logger.debug("[submit] telephone = " + telephone);
			logger.debug("[submit] fax = " + fax);
			logger.debug("[submit] cellphone = " + cellphone);
			logger.debug("[submit] qq = " + qq);
			logger.debug("[submit] weibo = " + weibo);
			logger.debug("[submit] mailAddress = " + mailAddress);
			logger.debug("[submit] accountPrivacy = " + accountPrivacy);
			logger.debug("[submit] contactPrivacy = " + contactPrivacy);
		}

		// validation
		if (submitValidator == null || submitValidator.validate(request)) {
			ObjectContainer objc = Tutor.getCurrentContainer();

			OrganizationModel orgModel = SessionContainer.getLoginUser(request);
			TeacherModel teacherModel;
			if (StringUtils.isNotEmpty(id)) {
				teacherModel = objc.ext().getByID(Long.parseLong(id));
				if (!orgModel.equals(teacherModel.getOrganization())) {
					// other teacher
					// TODO
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
					return false;
				}
			} else {
				teacherModel = new TeacherModel();
				teacherModel.setOrganization(orgModel);
			}
			boolean emailChanged = !StringUtils.equals(email,
					teacherModel.getEmail());
			String orgLock = ModelFactory.getUserSemaphore(orgModel.getEmail());
			String teacherLock = ModelFactory.getUserSemaphore(teacherModel
					.getEmail());
			String newLock = ModelFactory.getUserSemaphore(email);

			if (objc.ext().setSemaphore(orgLock, 0)
					&& (teacherLock == null || objc.ext().setSemaphore(
							teacherLock, 0))) {
				try {
					if (emailChanged
							&& (!objc.ext().setSemaphore(newLock, 0) || objc
									.queryByExample(
											ModelFactory.getUserExample(email))
									.hasNext())) {
						Messages.addError(request, "email",
								duplicatedEmailMessage);
					} else {
						// teacher info
						teacherModel.setEmail(Tutor.emptyNull(email));
						if (StringUtils.isNotEmpty(password)) {
							teacherModel.setPassword(ModelFactory
									.encryptPassword(password));
						}
						teacherModel.setName(Tutor.emptyNull(name));
						teacherModel.setMale("Male".equals(gender));
						teacherModel.setBirthday(StringUtils
								.join(birthday, '-'));
						teacherModel.setProvince(Tutor.emptyNull(province));
						teacherModel.setCity(Tutor.emptyNull(city));
						teacherModel.setDistrict(Tutor.emptyNull(district));
						if (subjects != null) {
							teacherModel.getSubjectsFor().clear();
							for (String s : subjects) {
								if (StringUtils.isEmpty(s))
									continue;
								teacherModel.getSubjectsFor().add(s);
							}
						}
						teacherModel.setGrade(grade);
						teacherModel.setGradeLevelFrom(Byte
								.valueOf(gradeLevelFrom));
						teacherModel
								.setGradeLevelTo(Byte.valueOf(gradeLevelTo));
						teacherModel.setEducation(Tutor.emptyNull(education));
						teacherModel.setTeachedYears(Tutor
								.byteNull(teachedYears));
						if (speciality != null) {
							teacherModel.getSpecialityFor().clear();
							for (String s : speciality) {
								if (StringUtils.isEmpty(s))
									continue;
								teacherModel.getSpecialityFor().add(s);
							}
						}
						teacherModel.setHomepage(Tutor.emptyNull(homepage));
						teacherModel.setDescription(Tutor
								.emptyNull(description));
						teacherModel.setAccountPrivacy(Tutor.enumNull(
								UserPrivacy.class, accountPrivacy));
						teacherModel.setContactPrivacy(Tutor.enumNull(
								UserPrivacy.class, contactPrivacy));
						teacherModel.setState(UserState.Activated);
						// contact
						UserContactModel contactModel = teacherModel
								.getContact();
						if (contactModel == null) {
							contactModel = new UserContactModel();
							teacherModel.setContact(contactModel);
						}
						contactModel.setName(Tutor.emptyNull(contactName));
						contactModel.setPostcode(Tutor.emptyNull(postcode));
						contactModel.setAddress1(Tutor.emptyNull(address1));
						contactModel.setAddress2(Tutor.emptyNull(address2));
						contactModel.setTelephone(Tutor.emptyNull(telephone));
						contactModel.setFax(Tutor.emptyNull(fax));
						contactModel.setCellphone(Tutor.emptyNull(cellphone));
						contactModel.setQQ(Tutor.emptyNull(qq));
						contactModel.setWeibo(Tutor.emptyNull(weibo));
						contactModel.setMailAddress(Tutor
								.emptyNull(mailAddress));
						// organization
						if (StringUtils.isEmpty(id)) {
							Integer count = orgModel.getTeacherCount();
							orgModel.setTeacherCount(count == null ? 1
									: count + 1);
						}
						// save
						objc.store(teacherModel);
						objc.commit();
						if (logger.isDebugEnabled())
							logger.debug("[submit] success. goto "
									+ submitSuccessPage);
						response.sendRedirect(request.getContextPath()
								+ submitSuccessPage);
						return false;
					}
				} catch (Throwable t) {
					logger.error("[submit] failed", t);
					objc.rollback();
					throw t;
				} finally {
					objc.ext().releaseSemaphore(orgLock);
					if (teacherLock != null)
						objc.ext().releaseSemaphore(teacherLock);
					if (emailChanged)
						objc.ext().releaseSemaphore(newLock);
				}
			}
			Messages.addError(request, "email", userLockedMessage);
		}

		setVariables(request);
		return true;
	}

	protected boolean delete(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[delete] called");

		// parameters
		String id = request.getParameter("id");
		if (logger.isInfoEnabled())
			logger.info("[delete] id = " + id);

		// validation
		if (deleteValidator == null || deleteValidator.validate(request)) {
			ObjectContainer objc = Tutor.getCurrentContainer();

			OrganizationModel orgModel = SessionContainer.getLoginUser(request);
			TeacherModel teacherModel = objc.ext().getByID(Long.parseLong(id));
			if (!orgModel.equals(teacherModel.getOrganization())) {
				// other teacher
				// TODO
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return false;
			}

			String lock = ModelFactory.getUserSemaphore(orgModel.getEmail());
			if (objc.ext().setSemaphore(lock, 0)) {
				try {
					teacherModel.setDeleted(Boolean.TRUE);
					orgModel.setTeacherCount(orgModel.getTeacherCount() - 1);
					objc.store(teacherModel);
					objc.commit();
				} catch (Throwable t) {
					logger.error("[delete] failed.", t);
					objc.rollback();
					throw t;
				} finally {
					objc.ext().releaseSemaphore(lock);
				}
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
	 * @return the deleteValidator
	 */
	public ParamValidator getDeleteValidator() {
		return deleteValidator;
	}

	/**
	 * @param deleteValidator
	 *            the deleteValidator to set
	 */
	public void setDeleteValidator(ParamValidator deleteValidator) {
		this.deleteValidator = deleteValidator;
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
	 * @return the submitSuccessPage
	 */
	public String getSubmitSuccessPage() {
		return submitSuccessPage;
	}

	/**
	 * @param submitSuccessPage
	 *            the submitSuccessPage to set
	 */
	public void setSubmitSuccessPage(String submitSuccessPage) {
		this.submitSuccessPage = submitSuccessPage;
	}

	/**
	 * @return the deleteSuccessPage
	 */
	public String getDeleteSuccessPage() {
		return deleteSuccessPage;
	}

	/**
	 * @param deleteSuccessPage
	 *            the deleteSuccessPage to set
	 */
	public void setDeleteSuccessPage(String deleteSuccessPage) {
		this.deleteSuccessPage = deleteSuccessPage;
	}

}
