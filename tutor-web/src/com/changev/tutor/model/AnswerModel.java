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

	public void clone(AnswerModel another) {
		super.clone(another);
		this.setAnswerDateTime(getAnswerDateTime());
		this.setAnswerer(another.getAnswerer());
		this.setOrganization(another.getOrganization());
		this.setQuestion(another.getQuestion());
		this.setInquery(another.getInquery());
		this.setAnswer(another.getAnswer());
	}

	@Override
	public AnswerModel clone() {
		return (AnswerModel) super.clone();
	}

	/**
	 * @return the answerDateTime
	 */
	public Date getAnswerDateTime() {
		return answerDateTime;
	}

	/**
	 * @param answerDateTime
	 *            the answerDateTime to set
	 */
	public void setAnswerDateTime(Date answerDateTime) {
		this.answerDateTime = answerDateTime;
	}

	/**
	 * @return the answerer
	 */
	public UserModel getAnswerer() {
		return answerer;
	}

	/**
	 * @param answerer
	 *            the answerer to set
	 */
	public void setAnswerer(UserModel answerer) {
		this.answerer = answerer;
	}

	/**
	 * @return the organization
	 */
	public OrganizationModel getOrganization() {
		return organization;
	}

	/**
	 * @param organization
	 *            the organization to set
	 */
	public void setOrganization(OrganizationModel organization) {
		this.organization = organization;
	}

	/**
	 * @return the question
	 */
	public QuestionModel getQuestion() {
		return question;
	}

	/**
	 * @param question
	 *            the question to set
	 */
	public void setQuestion(QuestionModel question) {
		this.question = question;
	}

	/**
	 * @return the inquery
	 */
	public String getInquery() {
		return inquery;
	}

	/**
	 * @param inquery
	 *            the inquery to set
	 */
	public void setInquery(String inquery) {
		this.inquery = inquery;
	}

	/**
	 * @return the answer
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * @param answer
	 *            the answer to set
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
