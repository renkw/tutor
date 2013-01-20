/*
 * File   MemberView.java
 * Create 2013/01/20
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.back;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.QuestionModel;
import com.changev.tutor.model.TeacherModel;
import com.changev.tutor.model.UserModel;
import com.changev.tutor.model.UserRole;
import com.changev.tutor.web.SessionContainer;
import com.changev.tutor.web.View;
import com.db4o.query.Predicate;
import com.db4o.query.QueryComparator;

/**
 * <p>
 * 用户首页。
 * </p>
 * 
 * @author ren
 * 
 */
public class MemberView implements View {

	private static final Logger logger = Logger.getLogger(MemberView.class);

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");

		setVariables(request);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[postRender] called");
	}

	@SuppressWarnings("serial")
	protected void setVariables(HttpServletRequest request) {
		if (logger.isTraceEnabled())
			logger.trace("[setVariables] called");

		// questions
		UserModel loginUser = SessionContainer.getLoginUser(request);
		if (loginUser.getRole() == UserRole.Teacher) {
			final TeacherModel teacher = (TeacherModel) loginUser;
			List<QuestionModel> questions = Tutor.getCurrentContainer().query(
					new Predicate<QuestionModel>() {
						@Override
						public boolean match(QuestionModel candidate) {
							return (candidate.getSpecifiedAnswerer() == teacher || (candidate.getProvince() == teacher.getProvince()
									&& candidate.getCity() == teacher.getCity()
									&& candidate.getDistrict() == teacher.getDistrict()
									&& candidate.getGrade() == teacher.getGrade()
									&& candidate.getGradeLevel() >= teacher.getGradeLevelFrom()
									&& candidate.getGradeLevel() <= teacher.getGradeLevelTo() && teacher
									.getSubjects().contains(
											candidate.getSubject())))
									&& !candidate.getClosed()
									&& !candidate.getDeleted();
						}
					}, new QueryComparator<QuestionModel>() {
						@Override
						public int compare(QuestionModel first,
								QuestionModel second) {
							return second.getCreateDateTime().compareTo(
									first.getCreateDateTime());
						}
					});
			if (questions.size() > 20)
				questions = questions.subList(0, 20);
			request.setAttribute("questions", questions);
		}
	}
}
