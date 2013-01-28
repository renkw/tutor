/*
 * File   QuestionListView.java
 * Create 2013/01/24
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.front;

import static com.changev.tutor.model.AbstractModel.CREATE_DATE_TIME;
import static com.changev.tutor.model.AbstractModel.DELETED;
import static com.changev.tutor.model.QuestionModel.ANSWERED;
import static com.changev.tutor.model.QuestionModel.ASSIGN_TO;
import static com.changev.tutor.model.QuestionModel.CLOSED;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.QuestionModel;
import com.changev.tutor.model.UserModel;
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
		final UserModel loginUser = container.getLoginUser();
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

			Date toDate = calendar.getTime();
			// get answered questions
			QueryBuilder<QuestionModel> q = new QueryBuilder<QuestionModel>();
			if ("new".equals(sort)) {
				q.isFalse(ANSWERED);
			} else if ("old".equals(sort)) {
				q.isTrue(ANSWERED);
			} else if ("close".equals(sort)) {
				q.isTrue(CLOSED);
			}

			if (questionList == null) {
				questionList = q.isFalse(DELETED).ge(toDate, CREATE_DATE_TIME)
						.or(new SubQuery() {
							@Override
							public void query(QueryBuilder<?> q) {
								q.eq(loginUser, ASSIGN_TO).isNull(ASSIGN_TO);
							}
						}).select();
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
				.append(pageno).toString());
	}

}
