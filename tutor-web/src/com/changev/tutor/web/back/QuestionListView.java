/*
 * File   QuestionListView.java
 * Create 2013/01/24
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.back;

import static com.changev.tutor.model.AbstractModel.CREATE_DATE_TIME;
import static com.changev.tutor.model.AbstractModel.DELETED;
import static com.changev.tutor.model.QuestionModel.ASSIGN_TO;
import static com.changev.tutor.model.QuestionModel.CITY;
import static com.changev.tutor.model.QuestionModel.CLOSED;
import static com.changev.tutor.model.QuestionModel.EXPIRATION_DATE;
import static com.changev.tutor.model.QuestionModel.GRADE;
import static com.changev.tutor.model.QuestionModel.GRADE_LEVEL;
import static com.changev.tutor.model.QuestionModel.PROVINCE;
import static com.changev.tutor.model.QuestionModel.SUBJECT;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.AnswerModel;
import com.changev.tutor.model.QuestionModel;
import com.changev.tutor.model.TeacherModel;
import com.changev.tutor.model.UserRole;
import com.changev.tutor.util.QueryBuilder;
import com.changev.tutor.util.QueryBuilder.SubQuery;
import com.changev.tutor.web.SessionContainer;
import com.changev.tutor.web.View;

/**
 * <p>
 * 提问列表。
 * </p>
 * 
 * @author ren
 * 
 */
public class QuestionListView implements View {

	private static final Logger logger = Logger
			.getLogger(QuestionListView.class);

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
		searchQuestions(request);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[postRender] called");
	}

	protected void searchQuestions(HttpServletRequest request) {
		if (logger.isTraceEnabled())
			logger.trace("[searchQuestions] called");

		// parameters
		String range = StringUtils.defaultIfEmpty(
				request.getParameter("range"), "month");
		String sort = StringUtils.defaultIfEmpty(request.getParameter("sort"),
				"new");
		String pageno = StringUtils.defaultIfEmpty(
				request.getParameter("pageno"), "1");
		String search = request.getParameter("search");
		if (logger.isDebugEnabled()) {
			logger.debug("[searchQuestions] range = " + range);
			logger.debug("[searchQuestions] sort = " + sort);
			logger.debug("[searchQuestions] pageno = " + pageno);
			logger.debug("[searchQuestions] search = " + search);
		}

		SessionContainer container = SessionContainer.get(request);
		final TeacherModel loginUser = container.getLoginUser().as(
				UserRole.Teacher);
		List<QuestionModel> questionList = container.getQuestionList();
		if (questionList == null || StringUtils.isNotEmpty(search)) {
			if (logger.isDebugEnabled())
				logger.debug("[search] do search");

			Calendar calendar = Tutor.currentCalendar();
			if ("week".equals(range))
				calendar.add(Calendar.DATE, -7);
			else if ("month".equals(range))
				calendar.add(Calendar.MONTH, -1);
			else if ("quarter".equals(range))
				calendar.add(Calendar.MONTH, -3);
			else if ("half".equals(range))
				calendar.add(Calendar.MONTH, -6);
			else if ("all".equals(range))
				calendar.clear();

			Date toDate = calendar.getTime();
			Date now = Tutor.currentDateTime();
			// get answered questions
			List<AnswerModel> answerList = loginUser.getAnswers();
			List<QuestionModel> answeredList = new ArrayList<QuestionModel>(
					answerList.size());
			for (AnswerModel answerModel : answerList)
				answeredList.add(answerModel.getQuestion());
			if ("old".equals(sort)) {
				questionList = answeredList;
			} else {
				QueryBuilder<QuestionModel> q = new QueryBuilder<QuestionModel>();
				q.isFalse(CLOSED).isFalse(DELETED).ge(toDate, CREATE_DATE_TIME)
						.gt(now, EXPIRATION_DATE).or(new SubQuery() {
							@Override
							public void query(QueryBuilder<?> q) {
								q.eq(loginUser, ASSIGN_TO).and(new SubQuery() {
									@Override
									public void query(QueryBuilder<?> q) {
										q.eq(loginUser.getProvince(), PROVINCE)
												.eq(loginUser.getCity(), CITY)
												.eq(loginUser.getGrade(), GRADE)
												.range(loginUser
														.getGradeLevelFrom(),
														loginUser
																.getGradeLevelTo(),
														GRADE_LEVEL)
												.in(loginUser.getSubjects(),
														SUBJECT);
									}
								});
							}
						});
				questionList = q.execute();
				container.setQuestionList(questionList);
			}
		}

		// set variables
		final int items = 10;
		int size = questionList.size(), start = items
				* (Integer.parseInt(pageno) - 1);
		request.setAttribute("total", size);
		request.setAttribute("totalPages", (size + items - 1) / items);
		request.setAttribute("questions",
				Tutor.listDesc(questionList, size - start - 1, items));
	}
}
