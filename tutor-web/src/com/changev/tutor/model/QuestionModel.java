/*
 * File   QuestionModel.java
 * Create 2013/01/12
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 提问。
 * </p>
 * 
 * @author ren
 * 
 */
public class QuestionModel extends AbstractModel {

	private static final long serialVersionUID = -1143983407388574974L;

	private UserModel questioner;
	private StudentModel student;
	private String province;
	private String city;
	private String district;
	private String subject;
	private String title;
	private UserModel specifiedAnswerer;
	private Date questionDateTime;
	private Date questionExpiration;
	private Date finalAnswerDateTime;
	private UserModel finalAnswerer;
	private List<String> uploadPictures;
	private List<AnswerModel> answers;

	public void clone(QuestionModel another) {
		super.clone(another);
		this.setQuestioner(another.getQuestioner());
		this.setStudent(another.getStudent());
		this.setProvince(another.getProvince());
		this.setCity(another.getCity());
		this.setDistrict(another.getDistrict());
		this.setSubject(another.getSubject());
		this.setTitle(another.getTitle());
		this.setSpecifiedAnswerer(another.getSpecifiedAnswerer());
		this.setQuestionDateTime(another.getQuestionDateTime());
		this.setFinalAnswerDateTime(another.getFinalAnswerDateTime());
		this.setFinalAnswerer(another.getFinalAnswerer());
		this.setUploadPictures(another.getUploadPictures());
	}

	@Override
	public QuestionModel clone() {
		return (QuestionModel) super.clone();
	}

	/**
	 * @return the questioner
	 */
	public UserModel getQuestioner() {
		return questioner;
	}

	/**
	 * @param questioner
	 *            the questioner to set
	 */
	public void setQuestioner(UserModel questioner) {
		this.questioner = questioner;
	}

	/**
	 * @return the student
	 */
	public StudentModel getStudent() {
		return student;
	}

	/**
	 * @param student
	 *            the student to set
	 */
	public void setStudent(StudentModel student) {
		this.student = student;
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

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the specifiedAnswerer
	 */
	public UserModel getSpecifiedAnswerer() {
		return specifiedAnswerer;
	}

	/**
	 * @param specifiedAnswerer
	 *            the specifiedAnswerer to set
	 */
	public void setSpecifiedAnswerer(UserModel specifiedAnswerer) {
		this.specifiedAnswerer = specifiedAnswerer;
	}

	/**
	 * @return the questionDateTime
	 */
	public Date getQuestionDateTime() {
		return questionDateTime;
	}

	/**
	 * @param questionDateTime
	 *            the questionDateTime to set
	 */
	public void setQuestionDateTime(Date questionDateTime) {
		this.questionDateTime = questionDateTime;
	}

	/**
	 * @return the questionExpiration
	 */
	public Date getQuestionExpiration() {
		return questionExpiration;
	}

	/**
	 * @param questionExpiration
	 *            the questionExpiration to set
	 */
	public void setQuestionExpiration(Date questionExpiration) {
		this.questionExpiration = questionExpiration;
	}

	/**
	 * @return the finalAnswerDateTime
	 */
	public Date getFinalAnswerDateTime() {
		return finalAnswerDateTime;
	}

	/**
	 * @param finalAnswerDateTime
	 *            the finalAnswerDateTime to set
	 */
	public void setFinalAnswerDateTime(Date finalAnswerDateTime) {
		this.finalAnswerDateTime = finalAnswerDateTime;
	}

	/**
	 * @return the finalAnswerer
	 */
	public UserModel getFinalAnswerer() {
		return finalAnswerer;
	}

	/**
	 * @param finalAnswerer
	 *            the finalAnswerer to set
	 */
	public void setFinalAnswerer(UserModel finalAnswerer) {
		this.finalAnswerer = finalAnswerer;
	}

	/**
	 * @return the uploadPictures
	 */
	public List<String> getUploadPictures() {
		return uploadPictures;
	}

	/**
	 * @param uploadPictures
	 *            the uploadPictures to set
	 */
	public void setUploadPictures(List<String> uploadPictures) {
		this.uploadPictures = uploadPictures;
	}

	/**
	 * @return the answers
	 */
	public List<AnswerModel> getAnswers() {
		return answers;
	}

	/**
	 * @param answers
	 *            the answers to set
	 */
	public void setAnswers(List<AnswerModel> answers) {
		this.answers = answers;
	}

}
