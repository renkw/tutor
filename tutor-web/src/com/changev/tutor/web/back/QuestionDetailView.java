/*
 * File   QuestionDetailView.java
 * Create 2013/01/24
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.back;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.AbstractModel;
import com.changev.tutor.model.AnswerDetailModel;
import com.changev.tutor.model.AnswerModel;
import com.changev.tutor.model.QuestionModel;
import com.changev.tutor.model.TeacherModel;
import com.changev.tutor.model.UserRole;
import com.changev.tutor.web.SessionContainer;
import com.changev.tutor.web.View;
import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;

/**
 * <p>
 * 提问解答。
 * </p>
 * 
 * @author ren
 * 
 */
public class QuestionDetailView implements View {

	private static final Logger logger = Logger
			.getLogger(QuestionListView.class);

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
			logger.trace("[postRender] called");
	}

	protected boolean submit(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[submit] called");

		String id = request.getParameter("id");
		String answerId = request.getParameter("answerId");
		String answer = request.getParameter("answer");
		if (logger.isDebugEnabled()) {
			logger.debug("[submit] id = " + id);
			logger.debug("[submit] answerId = " + answerId);
			logger.debug("[submit] answer = " + answer);
		}

		TeacherModel loginUser = SessionContainer.getLoginUser(request).as(
				UserRole.Teacher);
		ObjectContainer objc = Tutor.getCurrentContainer();
		try {
			AbstractModel model;
			if (StringUtils.isEmpty(answerId)) {
				QuestionModel questionModel = Tutor.fromId(id);
				AnswerModel answerModel = new AnswerModel();
				answerModel.setQuestion(questionModel);
				answerModel.setAnswerer(loginUser);
				answerModel.setAnswer(answer);
				model = answerModel;
			} else {
				model = Tutor.fromId(answerId);
				if (model instanceof AnswerModel) {
					AnswerModel answerModel = (AnswerModel) model;
					answerModel.setAnswer(Tutor.emptyNull(answer));
				} else if (model instanceof AnswerDetailModel) {
					AnswerDetailModel detailModel = (AnswerDetailModel) model;
					detailModel.setAnswer(Tutor.emptyNull(answer));
				} else {
					// illegal id
					// TODO
				}
			}
			objc.store(model);
			objc.commit();
		} catch (Throwable t) {
			logger.error("[submit] save answer error", t);
			objc.rollback();
			throw t;
		}

		setVariables(request);
		return true;
	}

	@SuppressWarnings("serial")
	protected void setVariables(HttpServletRequest request) {
		if (logger.isTraceEnabled())
			logger.trace("[setVariables] called");

		String id = request.getParameter("id");
		if (logger.isDebugEnabled())
			logger.debug("[setVariables] id = " + id);

		final TeacherModel loginUser = SessionContainer.getLoginUser(request)
				.as(UserRole.Teacher);
		final QuestionModel questionModel = Tutor.fromId(id);
		AnswerModel answerModel = Tutor.one(Tutor.getCurrentContainer().query(
				new Predicate<AnswerModel>() {
					@Override
					public boolean match(AnswerModel candidate) {
						return candidate.getAnswerer() == loginUser
								&& candidate.getQuestion() == questionModel
								&& candidate.getDeleted() == Boolean.FALSE;
					}
				}));

		request.setAttribute("question", questionModel);
		request.setAttribute("answer", answerModel);
		List<AnswerDetailModel> detailList = Collections.emptyList();
		if (answerModel != null && answerModel.getDetails() != null
				&& !answerModel.getDetails().isEmpty()) {
			detailList = new ArrayList<AnswerDetailModel>(
					answerModel.getDetails());
		}
		AnswerDetailModel lastModel;
		if (detailList.isEmpty()) {
			lastModel = new AnswerDetailModel();
			lastModel.setQuestion(questionModel.getTitle());
			lastModel.setCreateDateTime(questionModel.getCreateDateTime());
			lastModel.setUpdateDateTime(Tutor.currentTimestamp());
		} else {
			lastModel = detailList.remove(detailList.size() - 1);
			request.setAttribute("answerId", Tutor.id(lastModel));
		}
		request.setAttribute("lastAnswer", lastModel);
		Collections.reverse(detailList);
		request.setAttribute("details", detailList);
	}

}
