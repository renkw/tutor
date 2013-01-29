/*
 * File   QuestionListView.java
 * Create 2013/01/24
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.back;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.AnswerModel;
import com.changev.tutor.model.ModelFactory;
import com.changev.tutor.model.QuestionModel;
import com.changev.tutor.model.TeacherModel;
import com.changev.tutor.model.UserRole;
import com.changev.tutor.web.SessionContainer;
import com.changev.tutor.web.View;
import com.db4o.query.Predicate;

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

	@SuppressWarnings({ "serial" })
	protected void searchQuestions(HttpServletRequest request) {
		if (logger.isTraceEnabled())
			logger.trace("[searchQuestions] called");

		// parameters
		String sort = request.getParameter("sort");
		String range = request.getParameter("range");
		String pageno = request.getParameter("pageno");
		if (logger.isDebugEnabled()) {
			logger.debug("[searchQuestions] sort = " + sort);
			logger.debug("[searchQuestions] range = " + range);
			logger.debug("[searchQuestions] pageno = " + pageno);
		}

		sort = StringUtils.defaultIfEmpty(sort, "new");
		range = StringUtils.defaultIfEmpty(range, "month");

		SessionContainer container = SessionContainer.get(request);
		final TeacherModel teacher = container.getLoginUser().as(
				UserRole.Teacher);
		List<QuestionModel> questionList = container.getQuestionList();
		if (questionList == null || StringUtils.isEmpty(pageno)) {
			if (logger.isDebugEnabled())
				logger.debug("[search] do search");

			questionList = null;
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

			final Date toDate = calendar.getTime();
			// get answered questions
			if ("new".equals(sort)) {
				final List<QuestionModel> oldQuestionList = ModelFactory
						.getAnswerQuestionList(teacher.getAnswers());
				questionList = Tutor.getCurrentContainer().query(
						new Predicate<QuestionModel>() {
							@Override
							public boolean match(QuestionModel candidate) {
								return !candidate.getDeleted()
										&& !candidate.getClosed()
										&& candidate.getCreateDateTime()
												.compareTo(toDate) >= 0
										&& (candidate.getAssignTo() == teacher
												|| candidate.getAssignTo() == teacher
														.getOrganization() || candidate
												.getAssignTo() == null)
										&& !oldQuestionList.contains(candidate);
							}
						});
			} else if ("old".equals(sort)) {
				List<AnswerModel> list = Tutor.getCurrentContainer().query(
						new Predicate<AnswerModel>() {
							@Override
							public boolean match(AnswerModel candidate) {
								return !candidate.getDeleted()
										&& candidate.getAnswerer() == teacher
										&&!candidate.getQuestion().getClosed()
										&& candidate.getQuestion()
												.getCreateDateTime()
												.compareTo(toDate) >= 0;
							}
						});
				questionList = ModelFactory.getAnswerQuestionList(list);
			} else if ("close".equals(sort)) {
				questionList = Tutor.getCurrentContainer().query(
						new Predicate<QuestionModel>() {
							@Override
							public boolean match(QuestionModel candidate) {
								return !candidate.getDeleted()
										&& candidate.getClosed()
										&& candidate.getCreateDateTime()
												.compareTo(toDate) >= 0
										&& (candidate.getAssignTo() == teacher
												|| candidate.getAssignTo() == teacher
														.getOrganization() || candidate
												.getAssignTo() == null);
							}
						});
			} else if ("all".equals(sort)) {
				questionList = Tutor.getCurrentContainer().query(
						new Predicate<QuestionModel>() {
							@Override
							public boolean match(QuestionModel candidate) {
								return !candidate.getDeleted()
										&& candidate.getCreateDateTime()
												.compareTo(toDate) >= 0
										&& (candidate.getAssignTo() == teacher
												|| candidate.getAssignTo() == teacher
														.getOrganization() || candidate
												.getAssignTo() == null);
							}
						});
			}

			container.setQuestionList(questionList);
		}

		// set variables
		final int items = 10;
		int size = questionList.size();
		int pn = StringUtils.isEmpty(pageno) ? 1 : Math.max(1,
				Math.min(Integer.parseInt(pageno), (size + items - 1) / items));
		int start = items * (pn - 1);
		questionList = Tutor.listDesc(questionList, size - start - 1, items);

		request.setAttribute("sort", sort);
		request.setAttribute("range", range);
		request.setAttribute("pageno", pn);
		request.setAttribute("total", size);
		request.setAttribute("totalPages", (size + items - 1) / items);
		request.setAttribute("questions", questionList);

		container.setQuestionListQuery(new StringBuilder("?sort=").append(sort)
				.append("&amp;range=").append(range).append("&amp;pageno=")
				.append(pn).append("&amp;search=s").toString());
	}

}
