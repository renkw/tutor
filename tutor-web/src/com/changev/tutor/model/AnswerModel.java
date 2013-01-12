/*
 * File   AnswerModel.java
 * Create 2013/01/12
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.Date;

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
	private UserModel answerer;
	private OrganizationModel organization;
	private String question;
	private String answer;
	private String province;
	private String city;
	private String district;

	public void clone(AnswerModel another) {
		super.clone(another);
		this.setAnswerDateTime(getAnswerDateTime());
		this.setAnswerer(another.getAnswerer());
		this.setOrganization(another.getOrganization());
		this.setAnswer(another.getAnswer());
		this.setProvince(another.getProvince());
		this.setCity(another.getCity());
		this.setDistrict(another.getDistrict());
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

	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @param province
	 *            the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * @param district
	 *            the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

}
