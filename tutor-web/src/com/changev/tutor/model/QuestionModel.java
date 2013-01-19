/*
 * File   QuestionModel.java
 * Create 2013/01/12
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.Date;
import java.util.List;

import com.db4o.ObjectContainer;
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
	private Byte gradeLevel;
	private String title;
	@Indexed
	private TeacherModel specifiedAnswerer;
	private Date questionExpiration;
	private Boolean closed;
	private Date closedDateTime;
	private TeacherModel finalAnswerer;
	private List<String> uploadPictures;

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
	public QuestionModel clone() {
		return (QuestionModel) super.clone();
	}

	public List<AnswerModel> getAnswers() {
		// TODO
		throw new UnsupportedOperationException();
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
	public Byte getGradeLevel() {
		beforeGet();
		return gradeLevel;
	}

	/**
	 * @param gradeLevel
	 *            the gradeLevel to set
	 */
	public void setGradeLevel(Byte gradeLevel) {
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
	public TeacherModel getSpecifiedAnswerer() {
		beforeGet();
		return specifiedAnswerer;
	}

	/**
	 * @param specifiedAnswerer
	 *            the specifiedAnswerer to set
	 */
	public void setSpecifiedAnswerer(TeacherModel specifiedAnswerer) {
		beforeSet();
		this.specifiedAnswerer = specifiedAnswerer;
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
	 * @return the closed
	 */
	public Boolean getClosed() {
		beforeGet();
		return closed;
	}

	/**
	 * @param closed
	 *            the closed to set
	 */
	public void setClosed(Boolean closed) {
		beforeSet();
		this.closed = closed;
	}

	/**
	 * @return the closedDateTime
	 */
	public Date getClosedDateTime() {
		beforeGet();
		return closedDateTime;
	}

	/**
	 * @param closedDateTime
	 *            the closedDateTime to set
	 */
	public void setClosedDateTime(Date closedDateTime) {
		beforeSet();
		this.closedDateTime = closedDateTime;
	}

	/**
	 * @return the finalAnswerer
	 */
	public TeacherModel getFinalAnswerer() {
		beforeGet();
		return finalAnswerer;
	}

	/**
	 * @param finalAnswerer
	 *            the finalAnswerer to set
	 */
	public void setFinalAnswerer(TeacherModel finalAnswerer) {
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
