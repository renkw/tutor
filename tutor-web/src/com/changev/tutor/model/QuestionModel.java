/*
 * File   QuestionModel.java
 * Create 2013/01/12
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.Date;
import java.util.List;

import com.db4o.collections.ActivatableArrayList;
import com.db4o.config.annotations.Indexed;

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

	@Indexed
	private UserModel questioner;
	private StudentModel student;
	private String province;
	private String city;
	private String district;
	private String subject;
	private String grade;
	private Integer gradeLevel;
	private String title;
	@Indexed
	private UserModel specifiedAnswerer;
	private Date questionDateTime;
	private Date questionExpiration;
	private Date finalAnswerDateTime;
	private UserModel finalAnswerer;
	private List<String> uploadPictures;

	@Override
	public QuestionModel clone() {
		return (QuestionModel) super.clone();
	}

	/**
	 * @return the questioner
	 */
	public UserModel getQuestioner() {
		beforeGet();
		return questioner;
	}

	/**
	 * @param questioner
	 *            the questioner to set
	 */
	public void setQuestioner(UserModel questioner) {
		beforeSet();
		this.questioner = questioner;
	}

	/**
	 * @return the student
	 */
	public StudentModel getStudent() {
		beforeGet();
		return student;
	}

	/**
	 * @param student
	 *            the student to set
	 */
	public void setStudent(StudentModel student) {
		beforeSet();
		this.student = student;
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		beforeGet();
		return province;
	}

	/**
	 * @param province
	 *            the province to set
	 */
	public void setProvince(String province) {
		beforeSet();
		this.province = province;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		beforeGet();
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		beforeSet();
		this.city = city;
	}

	/**
	 * @return the district
	 */
	public String getDistrict() {
		beforeGet();
		return district;
	}

	/**
	 * @param district
	 *            the district to set
	 */
	public void setDistrict(String district) {
		beforeSet();
		this.district = district;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		beforeGet();
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		beforeSet();
		this.subject = subject;
	}

	/**
	 * @return the grade
	 */
	public String getGrade() {
		beforeGet();
		return grade;
	}

	/**
	 * @param grade
	 *            the grade to set
	 */
	public void setGrade(String grade) {
		beforeSet();
		this.grade = grade;
	}

	/**
	 * @return the gradeLevel
	 */
	public Integer getGradeLevel() {
		beforeGet();
		return gradeLevel;
	}

	/**
	 * @param gradeLevel
	 *            the gradeLevel to set
	 */
	public void setGradeLevel(Integer gradeLevel) {
		beforeSet();
		this.gradeLevel = gradeLevel;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		beforeGet();
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		beforeSet();
		this.title = title;
	}

	/**
	 * @return the specifiedAnswerer
	 */
	public UserModel getSpecifiedAnswerer() {
		beforeGet();
		return specifiedAnswerer;
	}

	/**
	 * @param specifiedAnswerer
	 *            the specifiedAnswerer to set
	 */
	public void setSpecifiedAnswerer(UserModel specifiedAnswerer) {
		beforeSet();
		this.specifiedAnswerer = specifiedAnswerer;
	}

	/**
	 * @return the questionDateTime
	 */
	public Date getQuestionDateTime() {
		beforeGet();
		return questionDateTime;
	}

	/**
	 * @param questionDateTime
	 *            the questionDateTime to set
	 */
	public void setQuestionDateTime(Date questionDateTime) {
		beforeSet();
		this.questionDateTime = questionDateTime;
	}

	/**
	 * @return the questionExpiration
	 */
	public Date getQuestionExpiration() {
		beforeGet();
		return questionExpiration;
	}

	/**
	 * @param questionExpiration
	 *            the questionExpiration to set
	 */
	public void setQuestionExpiration(Date questionExpiration) {
		beforeSet();
		this.questionExpiration = questionExpiration;
	}

	/**
	 * @return the finalAnswerDateTime
	 */
	public Date getFinalAnswerDateTime() {
		beforeGet();
		return finalAnswerDateTime;
	}

	/**
	 * @param finalAnswerDateTime
	 *            the finalAnswerDateTime to set
	 */
	public void setFinalAnswerDateTime(Date finalAnswerDateTime) {
		beforeSet();
		this.finalAnswerDateTime = finalAnswerDateTime;
	}

	/**
	 * @return the finalAnswerer
	 */
	public UserModel getFinalAnswerer() {
		beforeGet();
		return finalAnswerer;
	}

	/**
	 * @param finalAnswerer
	 *            the finalAnswerer to set
	 */
	public void setFinalAnswerer(UserModel finalAnswerer) {
		beforeSet();
		this.finalAnswerer = finalAnswerer;
	}

	/**
	 * @return the uploadPictures
	 */
	public List<String> getUploadPictures() {
		beforeGet();
		return uploadPictures;
	}

	/**
	 * @return the uploadPictures
	 */
	public List<String> getUploadPicturesFor() {
		beforeGet();
		if (uploadPictures == null) {
			beforeSet();
			uploadPictures = new ActivatableArrayList<String>();
		}
		return uploadPictures;
	}

}
