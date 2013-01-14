/*
 * File   AnswerModel.java
 * Create 2013/01/12
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.Date;

import com.db4o.config.annotations.Indexed;

/**
 * <p>
 * 提问解答。
 * </p>
 * 
 * @author ren
 * 
 */
public class AnswerModel extends AbstractModel {

	private static final long serialVersionUID = 8353464399050892383L;

	private Date answerDateTime;
	@Indexed
	private UserModel answerer;
	private OrganizationModel organization;
	@Indexed
	private QuestionModel question;
	private String inquery;
	private String answer;

	@Override
	public AnswerModel clone() {
		return (AnswerModel) super.clone();
	}

	/**
	 * @return the answerDateTime
	 */
	public Date getAnswerDateTime() {
		beforeGet();
		return answerDateTime;
	}

	/**
	 * @param answerDateTime
	 *            the answerDateTime to set
	 */
	public void setAnswerDateTime(Date answerDateTime) {
		beforeSet();
		this.answerDateTime = answerDateTime;
	}

	/**
	 * @return the answerer
	 */
	public UserModel getAnswerer() {
		beforeGet();
		return answerer;
	}

	/**
	 * @param answerer
	 *            the answerer to set
	 */
	public void setAnswerer(UserModel answerer) {
		beforeSet();
		this.answerer = answerer;
	}

	/**
	 * @return the organization
	 */
	public OrganizationModel getOrganization() {
		beforeGet();
		return organization;
	}

	/**
	 * @param organization
	 *            the organization to set
	 */
	public void setOrganization(OrganizationModel organization) {
		beforeSet();
		this.organization = organization;
	}

	/**
	 * @return the question
	 */
	public QuestionModel getQuestion() {
		beforeGet();
		return question;
	}

	/**
	 * @param question
	 *            the question to set
	 */
	public void setQuestion(QuestionModel question) {
		beforeSet();
		this.question = question;
	}

	/**
	 * @return the inquery
	 */
	public String getInquery() {
		beforeGet();
		return inquery;
	}

	/**
	 * @param inquery
	 *            the inquery to set
	 */
	public void setInquery(String inquery) {
		beforeSet();
		this.inquery = inquery;
	}

	/**
	 * @return the answer
	 */
	public String getAnswer() {
		beforeGet();
		return answer;
	}

	/**
	 * @param answer
	 *            the answer to set
	 */
	public void setAnswer(String answer) {
		beforeSet();
		this.answer = answer;
	}

}
