/*
 * File   QuestionDetailView.java
 * Create 2013/01/24
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.front;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.AnswerModel;
import com.changev.tutor.model.QuestionModel;
import com.changev.tutor.util.QueryBuilder;
import com.changev.tutor.web.View;
import com.changev.tutor.web.util.ParamValidator;

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
	private String defaultBackUrl;

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
		if (StringUtils.isNotEmpty(request.getParameter("final")))
			return closeQuestion(request, response);
		setVariables(request, null);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[postRender] called");
	}

	protected boolean closeQuestion(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[closeQuesiton] called");

		String id = request.getParameter("id");
		String finalId = request.getParameter("final");
		if (logger.isDebugEnabled()) {
			logger.debug("[closeQuesiton] id = " + id);
			logger.debug("[closeQuesiton] final = " + finalId);
		}

		QuestionModel questionModel = Tutor.fromId(id);
		AnswerModel answerModel = Tutor.fromId(finalId);
		if (questionModel != null && answerModel != null
				&& !questionModel.getClosed()
				&& questionModel.equals(answerModel.getQuestion())) {
			try {
				questionModel.setClosed(Boolean.TRUE);
				questionModel.setFinalAnswerer(answerModel.getAnswerer());
				questionModel.setClosedDateTime(Tutor.currentDateTime());
				Tutor.getCurrentContainer().store(questionModel);
				Tutor.commitCurrent();
			} catch (Throwable t) {
				logger.error("[closeQuesiton] save error", t);
				Tutor.rollbackCurrent();
				throw t;
			}
		}

		setVariables(request, questionModel);
		return true;
	}

	protected void setVariables(HttpServletRequest request,
			QuestionModel questionModel) {
		if (logger.isTraceEnabled())
			logger.trace("[setVariables] called");

		String id = request.getParameter("id");
		if (logger.isDebugEnabled())
			logger.debug("[setVariables] id = " + id);

		if (questionModel == null)
			questionModel = Tutor.fromId(id);
		List<AnswerModel> answerList = new QueryBuilder<AnswerModel>()
				.eq(questionModel, AnswerModel.QUESTION)
				.isFalse(AnswerModel.DELETED).execute();
		request.setAttribute("question", questionModel);
		request.setAttribute("answers", answerList);
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

	/**
	 * @return the defaultBackUrl
	 */
	public String getDefaultBackUrl() {
		return defaultBackUrl;
	}

	/**
	 * @param defaultBackUrl
	 *            the defaultBackUrl to set
	 */
	public void setDefaultBackUrl(String defaultBackUrl) {
		this.defaultBackUrl = defaultBackUrl;
	}

}
