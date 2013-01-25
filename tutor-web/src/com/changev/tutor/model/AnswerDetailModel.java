/*
 * File   AnswerDetailModel.java
 * Create 2013/01/19
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import com.db4o.ObjectContainer;

/**
 * <p>
 * 回答追问。
 * </p>
 * 
 * @author ren
 * 
 */
public class AnswerDetailModel extends AbstractModel {

	private static final long serialVersionUID = 1491587760914605595L;

	public static final String QUESTION = "question";
	public static final String ANSWER = "answer";

	private String question;
	private String answer;

	@Override
	public void objectOnActivate(ObjectContainer container) {
		super.objectOnActivate(container);
	}

	@Override
	public void objectOnNew(ObjectContainer container) {
		super.objectOnNew(container);
	}

	@Override
	public void objectOnUpdate(ObjectContainer container) {
		super.objectOnUpdate(container);
	}

	@Override
	public AnswerDetailModel clone() {
		return (AnswerDetailModel) super.clone();
	}

	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * @param question
	 *            the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
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
