/*
 * File   RegisterStudentView.java
 * Create 2013/01/14
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.front;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.ModelFactory;
import com.changev.tutor.model.ParentModel;
import com.changev.tutor.model.StudentModel;
import com.changev.tutor.model.UserModel;
import com.changev.tutor.model.UserRole;
import com.changev.tutor.model.UserState;
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
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[postRender] called");
	}

	protected boolean registerStudent(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[registerStudent] called");
		// get parameters
		String name = request.getParameter("name");
		String gender = request.getParameter("gender");
		String[] birthday = request.getParameterValues("birthday");
		String province = request.getParameter("province");
		String city = request.getParameter("city");
		String district = request.getParameter("district");
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
			logger.debug("[registerStudent] province = " + province);
			logger.debug("[registerStudent] city = " + city);
			logger.debug("[registerStudent] district = " + district);
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
			ObjectContainer objc = Tutor.getCurrentContainer();
			UserModel parentModel = SessionContainer.getLoginUser(request);
			String lock = ModelFactory.getUserSemaphore(parentModel.getEmail());
			if (objc.ext().setSemaphore(lock, 0)) {
				if (parentModel.getRole() != UserRole.Parent
						|| parentModel.getState() != UserState.Incomplete) {
					// no need register
					if (logger.isDebugEnabled())
						logger.debug("[registerStudent] no need register. goto /home.html");
					response.sendRedirect(request.getContextPath() + "/");
					return false;
				}
				try {
					StudentModel studentModel = new StudentModel();
					studentModel.setName(name);
					studentModel.setMale("Male".equals(gender));
					// studentModel.setBirthday(birthday);
					studentModel.setProvince(province);
					studentModel.setCity(city);
					studentModel.setDistrict(district);
					studentModel.setSchool(school);
					studentModel.setGrade(grade);
					studentModel.setGradeLevel(Byte.valueOf(gradeLevel));
					studentModel.setHobby(hobby);
					studentModel.setDescription(description);
					studentModel.setParent((ParentModel) parentModel);
					// studentModel.setState(UserState.Activated);
					studentModel.setState(UserState.Unavailable);
					for (int i = 0; i < subject.length && i < answerer.length; i++) {
						if (StringUtils.isNotEmpty(answerer[i])) {
							studentModel.getDefaultAnswererFor().put(
									subject[i], answerer[i]);
						}
					}

					parentModel.setProvince(province);
					parentModel.setCity(city);
					parentModel.setDistrict(district);
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
			Messages.addError(request, "email", userLockedMessage);
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
