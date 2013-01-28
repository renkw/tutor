/*
 * File   QuestionDetailView.java
 * Create 2013/01/24
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.back;

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
import com.changev.tutor.util.QueryBuilder;
import com.changev.tutor.web.SessionContainer;
import com.changev.tutor.web.View;
import com.changev.tutor.web.util.ParamUtils;
import com.changev.tutor.web.util.ParamValidator;
import com.db4o.ObjectContainer;

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
			.getLogger(QuestionDetailView.class);

	private ParamValidator submitValidator;

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
		SessionContainer.get(request).setActionMessage(null);
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

		if (submitValidator == null || submitValidator.validate(request)) {
			if (logger.isDebugEnabled())
				logger.debug("[submit] validate passed");

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
					answerModel.setAnswer(ParamUtils.emptyNull(answer));
					model = answerModel;
				} else {
					model = Tutor.fromId(answerId);
					if (model instanceof AnswerModel) {
						AnswerModel answerModel = (AnswerModel) model;
						answerModel.setAnswer(ParamUtils.emptyNull(answer));
					} else if (model instanceof AnswerDetailModel) {
						AnswerDetailModel detailModel = (AnswerDetailModel) model;
						detailModel.setAnswer(ParamUtils.emptyNull(answer));
					} else {
						// illegal id
						// TODO
					}
				}
				objc.store(model);
				objc.commit();
				SessionContainer.get(request).setActionMessage("发布解答成功。");
			} catch (Throwable t) {
				logger.error("[submit] save answer error", t);
				objc.rollback();
				throw t;
			}
		}

		setVariables(request);
		return true;
	}

	protected void setVariables(HttpServletRequest request) {
		if (logger.isTraceEnabled())
			logger.trace("[setVariables] called");

		String id = request.getParameter("id");
		String backUrl = request.getParameter("backUrl");
		if (logger.isDebugEnabled()) {
			logger.debug("[setVariables] id = " + id);
			logger.debug("[setVariables] backUrl = " + backUrl);
		}

		TeacherModel loginUser = SessionContainer.getLoginUser(request).as(
				UserRole.Teacher);
		QuestionModel questionModel = Tutor.fromId(id);
		AnswerModel answerModel = Tutor.one(new QueryBuilder<AnswerModel>()
				.eq(loginUser, AnswerModel.ANSWERER)
				.eq(questionModel, AnswerModel.QUESTION)
				.isFalse(AnswerModel.DELETED).select());
		request.setAttribute("question", questionModel);
		request.setAttribute("answer", answerModel);
		List<AnswerDetailModel> detailList = Collections.emptyList();
		if (answerModel != null && answerModel.getDetails() != null)
			detailList = Tutor.listDesc(answerModel.getDetails());
		AnswerDetailModel lastModel;
		if (detailList.isEmpty()) {
			lastModel = new AnswerDetailModel();
			lastModel.setQuestion(questionModel.getTitle());
			lastModel.setCreateDateTime(questionModel.getCreateDateTime());
			if (answerModel != null) {
				lastModel.setAnswer(answerModel.getAnswer());
				lastModel.setUpdateDateTime(answerModel.getUpdateDateTime());
				request.setAttribute("answerId", Tutor.id(answerModel));
			} else {
				lastModel.setUpdateDateTime(Tutor.currentDateTime());
			}
		} else {
			lastModel = detailList.get(0);
			request.setAttribute("answerId", Tutor.id(lastModel));
		}
		request.setAttribute("lastAnswer", lastModel);
		request.setAttribute("details", detailList);
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

}
