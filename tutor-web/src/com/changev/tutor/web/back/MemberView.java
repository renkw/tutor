/*
 * File   MemberView.java
 * Create 2013/01/20
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.back;

import static com.changev.tutor.model.AbstractModel.DELETED;
import static com.changev.tutor.model.QuestionModel.ASSIGN_TO;
import static com.changev.tutor.model.QuestionModel.CITY;
import static com.changev.tutor.model.QuestionModel.CLOSED;
import static com.changev.tutor.model.QuestionModel.GRADE;
import static com.changev.tutor.model.QuestionModel.GRADE_LEVEL;
import static com.changev.tutor.model.QuestionModel.PROVINCE;
import static com.changev.tutor.model.QuestionModel.SUBJECT;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.AnswerModel;
import com.changev.tutor.model.QuestionModel;
import com.changev.tutor.model.TeacherModel;
import com.changev.tutor.model.UserModel;
import com.changev.tutor.model.UserRole;
import com.changev.tutor.util.QueryBuilder;
import com.changev.tutor.util.QueryBuilder.SubQuery;
import com.changev.tutor.web.SessionContainer;
import com.changev.tutor.web.View;
import com.db4o.query.Candidate;
import com.db4o.query.Evaluation;

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
			final List<?> oldQuestionList = new QueryBuilder<AnswerModel>()
					.isFalse(DELETED).isFalse(AnswerModel.QUESTION, CLOSED)
					.eq(loginUser, AnswerModel.ANSWERER)
					.execute(AnswerModel.QUESTION);
			QueryBuilder<QuestionModel> q = new QueryBuilder<QuestionModel>()
					.eval(new Evaluation() {
						@Override
						public void evaluate(Candidate candidate) {
							candidate.include(!oldQuestionList
									.contains(candidate.getObject()));
						}
					});
			q.isFalse(CLOSED).isFalse(DELETED).or(new SubQuery() {
				@Override
				public void query(QueryBuilder<?> q) {
					q.eq(teacher, ASSIGN_TO).and(new SubQuery() {
						@Override
						public void query(QueryBuilder<?> q) {
							q.isNull(ASSIGN_TO)
									.eq(teacher.getProvince(), PROVINCE)
									.eq(teacher.getCity(), CITY)
									.eq(teacher.getGrade(), GRADE)
									.range(teacher.getGradeLevelFrom(),
											teacher.getGradeLevelTo(),
											GRADE_LEVEL)
									.in(teacher.getSubjects(), SUBJECT);
						}
					});
				}
			});
			List<QuestionModel> questionList = Tutor.listDesc(q.execute(), 10);
			request.setAttribute("questions", questionList);
		}
	}

}
