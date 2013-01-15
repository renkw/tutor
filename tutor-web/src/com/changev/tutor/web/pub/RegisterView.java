/*
 * File   RegisterView.java
 * Create 2013/01/14
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.pub;

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
	private ParamValidator registerStudentValidator;
	private ParamValidator registerCompleteValidator;
	private String duplicatedEmailMessage;
	private String userLockedMessage;
	private String registerAccountPage;
	private String registerStudentPage;
	private String registerCompletePage;

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
		if (StringUtils.isNotEmpty(request.getParameter("registerAccount")))
			return registerAccount(request, response);
		if (StringUtils.isNotEmpty(request.getParameter("registerStudent")))
			return registerStudent(request, response);
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
		if (registerAccountValidator == null
				|| registerAccountValidator.validate(request)) {
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
									+ registerStudentPage);
						response.sendRedirect(request.getContextPath()
								+ registerStudentPage);
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
			logger.debug("[registerStudent] subject = " + subject);
			logger.debug("[registerStudent] answerer = " + answerer);
		}
		// validation
		if (registerStudentValidator == null
				|| registerStudentValidator.validate(request)) {
			ObjectContainer objc = Tutor.getCurrentContainer();
			UserModel parentModel = SessionContainer.get(request)
					.getLoginUser();
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
					// parentModel.setState(UserState.NotActivated);
					parentModel.setState(UserState.Activated);
					objc.store(studentModel);
					objc.commit();
					if (logger.isDebugEnabled())
						logger.debug("[registerStudent] success. goto "
								+ registerCompletePage);
					response.sendRedirect(request.getContextPath()
							+ registerCompletePage);
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

	protected boolean registerComplete(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
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
	 * @return the registerStudentValidator
	 */
	public ParamValidator getRegisterStudentValidator() {
		return registerStudentValidator;
	}

	/**
	 * @param registerStudentValidator
	 *            the registerStudentValidator to set
	 */
	public void setRegisterStudentValidator(
			ParamValidator registerStudentValidator) {
		this.registerStudentValidator = registerStudentValidator;
	}

	/**
	 * @return the registerCompleteValidator
	 */
	public ParamValidator getRegisterCompleteValidator() {
		return registerCompleteValidator;
	}

	/**
	 * @param registerCompleteValidator
	 *            the registerCompleteValidator to set
	 */
	public void setRegisterCompleteValidator(
			ParamValidator registerCompleteValidator) {
		this.registerCompleteValidator = registerCompleteValidator;
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
	 * @return the registerAccountPage
	 */
	public String getRegisterAccountPage() {
		return registerAccountPage;
	}

	/**
	 * @param registerAccountPage
	 *            the registerAccountPage to set
	 */
	public void setRegisterAccountPage(String registerAccountPage) {
		this.registerAccountPage = registerAccountPage;
	}

	/**
	 * @return the registerStudentPage
	 */
	public String getRegisterStudentPage() {
		return registerStudentPage;
	}

	/**
	 * @param registerStudentPage
	 *            the registerStudentPage to set
	 */
	public void setRegisterStudentPage(String registerStudentPage) {
		this.registerStudentPage = registerStudentPage;
	}

	/**
	 * @return the registerCompletePage
	 */
	public String getRegisterCompletePage() {
		return registerCompletePage;
	}

	/**
	 * @param registerCompletePage
	 *            the registerCompletePage to set
	 */
	public void setRegisterCompletePage(String registerCompletePage) {
		this.registerCompletePage = registerCompletePage;
	}

}
