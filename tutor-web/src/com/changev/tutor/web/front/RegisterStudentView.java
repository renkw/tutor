/*
 * File   RegisterStudentView.java
 * Create 2013/01/14
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
import com.changev.tutor.model.TeacherModel;
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
 * 用户注册。
 * </p>
 * 
 * @author ren
 * 
 */
public class RegisterStudentView implements View {

	private static final Logger logger = Logger
			.getLogger(RegisterStudentView.class);

	private ParamValidator registerValidator;
	private String userLockedMessage;
	private String successPage;

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
		if (StringUtils.isNotEmpty(request.getParameter("registerStudent")))
			return registerStudent(request, response);
		setVariables(request);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[postRender] called");
	}

	protected void setVariables(HttpServletRequest request) {
		if (logger.isTraceEnabled())
			logger.trace("[setVariables] called");

		Gson gson = Tutor.getBeanFactory().getBean(Gson.class);
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
	}

	protected boolean registerStudent(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[registerStudent] called");
		// get parameters
		String name = request.getParameter("name");
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
		if (logger.isDebugEnabled()) {
			logger.debug("[registerStudent] name = " + name);
			logger.debug("[registerStudent] gender = " + gender);
			logger.debug("[registerStudent] birthday = "
					+ Arrays.toString(birthday));
			logger.debug("[registerStudent] area = " + Arrays.toString(area));
			logger.debug("[registerStudent] school = " + school);
			logger.debug("[registerStudent] grade = " + grade);
			logger.debug("[registerStudent] gradeLevel = " + gradeLevel);
			logger.debug("[registerStudent] hobby = " + hobby);
			logger.debug("[registerStudent] description = " + description);
			logger.debug("[registerStudent] subject = "
					+ Arrays.toString(subject));
			logger.debug("[registerStudent] answerer = "
					+ Arrays.toString(answerer));
		}
		// validation
		if (registerValidator == null || registerValidator.validate(request)) {
			if (logger.isDebugEnabled())
				logger.debug("[registerStudent] validation passed");

			ObjectContainer objc = Tutor.getCurrentContainer();
			ParentModel parentModel = SessionContainer.getLoginUser(request)
					.as(UserRole.Parent);
			if (parentModel == null
					|| parentModel.getState() != UserState.Incomplete) {
				// no need register
				if (logger.isDebugEnabled())
					logger.debug("[registerStudent] no need register. goto /");
				response.sendRedirect(request.getContextPath() + "/");
				return false;
			}

			String lock = ModelFactory.getUserSemaphore(parentModel.getEmail());
			if (objc.ext().setSemaphore(lock, 0)) {
				try {
					StudentModel studentModel = new StudentModel();
					studentModel.setName(ParamUtils.emptyNull(name));
					studentModel.setMale("Male".equals(gender));
					studentModel.setBirthday(StringUtils.join(birthday, '-'));
					studentModel.setSchool(ParamUtils.emptyNull(school));
					studentModel.setGrade(ParamUtils.emptyNull(grade));
					studentModel.setGradeLevel(ParamUtils.byteNull(gradeLevel));
					studentModel.setHobby(ParamUtils.emptyNull(hobby));
					studentModel.setDescription(ParamUtils
							.emptyNull(description));
					studentModel.setParent(parentModel);
					// studentModel.setState(UserState.Unavailable);
					studentModel.setState(UserState.Incomplete);
					studentModel.getDefaultAnswererFor().clear();
					parentModel.getContactersFor().clear();
					if (subject != null && answerer != null) {
						for (int i = 0; i < subject.length
								&& i < answerer.length; i++) {
							if (StringUtils.isEmpty(answerer[i]))
								continue;
							TeacherModel teacher = Tutor.one(objc
									.queryByExample(ModelFactory
											.getUserExample(answerer[i])));
							studentModel.getDefaultAnswererFor().put(
									subject[i], teacher);
							parentModel.getContactersFor().add(teacher);
						}
					}

					parentModel.setProvince(ParamUtils.emptyNull(area, 0));
					parentModel.setCity(ParamUtils.emptyNull(area, 1));
					parentModel.setDistrict(ParamUtils.emptyNull(area, 2));
					// parentModel.setState(UserState.NotActivated);
					parentModel.setState(UserState.Activated);

					objc.store(studentModel);
					objc.commit();
					if (logger.isDebugEnabled())
						logger.debug("[registerStudent] success. goto "
								+ successPage);
					response.sendRedirect(request.getContextPath()
							+ successPage);
					return false;
				} catch (Throwable t) {
					logger.error("[registerStudent] failed", t);
					objc.rollback();
					throw t;
				} finally {
					objc.ext().releaseSemaphore(lock);
				}
			}
			Messages.addError(request, "name", userLockedMessage);
		}

		setVariables(request);
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
