/*
 * File   AnswerModel.java
 * Create 2013/01/12
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.collections.ActivatableArrayList;
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

	public static final String ANSWERER = "answerer";
	public static final String ORGANIZATION = "organization";
	public static final String QUESTION = "question";
	public static final String ANSWER = "answer";
	public static final String DETAILS = "details";

	@Indexed
	private TeacherModel answerer;
	private OrganizationModel organization;
	@Indexed
	private QuestionModel question;
	private String answer;
	private List<AnswerDetailModel> details;

	/*
	 * 添加问题id
	 */
	private String question_id;

	@Override
	public void objectOnActivate(ObjectContainer container) {
		super.objectOnActivate(container);
	}

	@Override
	public void objectOnNew(ObjectContainer container) {
		super.objectOnNew(container);
		setOrganization(answerer.getOrganization());
	}

	@Override
	public void objectOnUpdate(ObjectContainer container) {
		super.objectOnUpdate(container);
	}

	@Override
	public AnswerModel clone() {
		return (AnswerModel) super.clone();
	}

	/**
	 * @return the answerer
	 */
	public TeacherModel getAnswerer() {
		beforeGet();
		return answerer;
	}

	/**
	 * @param answerer
	 *            the answerer to set
	 */
	public void setAnswerer(TeacherModel answerer) {
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

	/**
	 * @return the details
	 */
	public List<AnswerDetailModel> getDetails() {
		beforeGet();
		return details;
	}

	/**
	 * @return the details
	 */
	public List<AnswerDetailModel> getDetailsFor() {
		beforeGet();
		if (details == null) {
			beforeSet();
			details = new ActivatableArrayList<AnswerDetailModel>();
		}
		return details;
	}

	public String getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}
}
