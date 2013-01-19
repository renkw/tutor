/*
 * File   TeacherModel.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.collections.ActivatableArrayList;
import com.db4o.config.annotations.Indexed;

/**
 * <p>
 * 教师。
 * </p>
 * 
 * @author ren
 * 
 */
public class TeacherModel extends UserModel {

	private static final long serialVersionUID = -5724580114977645750L;

	private Boolean male;
	private String birthday;
	@Indexed
	private String subject;
	private String grade;
	private Byte gradeLevelFrom;
	private Byte gradeLevelTo;
	private String education;
	private Byte teachYears;
	private List<String> speciality;
	private Integer score;
	private String homepage;
	@Indexed
	private OrganizationModel organization;

	public TeacherModel() {
		super.setRole(UserRole.Teacher);
	}

	@Override
	public void setRole(UserRole role) {
	}

	@Override
	public UserContactModel getContact() {
		if (super.getContact() == null && getOrganization() != null)
			return getOrganization().getContact();
		return super.getContact();
	}

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
	public TeacherModel clone() {
		return (TeacherModel) super.clone();
	}

	public List<QuestionModel> getAssignedQuestions() {
		// TODO
		throw new UnsupportedOperationException();
	}

	public List<AnswerModel> getAnswers() {
		// TODO
		throw new UnsupportedOperationException();
	}

	public List<AnswerModel> getAcceptAnswers() {
		// TODO
		throw new UnsupportedOperationException();
	}

	/**
	 * @return the male
	 */
	public Boolean getMale() {
		beforeGet();
		return male;
	}

	/**
	 * @param male
	 *            the male to set
	 */
	public void setMale(Boolean male) {
		beforeSet();
		this.male = male;
	}

	/**
	 * @return the birthday
	 */
	public String getBirthday() {
		beforeGet();
		return birthday;
	}

	/**
	 * @param birthday
	 *            the birthday to set
	 */
	public void setBirthday(String birthday) {
		beforeSet();
		this.birthday = birthday;
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
	 * @return the gradeLevelFrom
	 */
	public Byte getGradeLevelFrom() {
		beforeGet();
		return gradeLevelFrom;
	}

	/**
	 * @param gradeLevelFrom
	 *            the gradeLevelFrom to set
	 */
	public void setGradeLevelFrom(Byte gradeLevelFrom) {
		beforeSet();
		this.gradeLevelFrom = gradeLevelFrom;
	}

	/**
	 * @return the gradeLevelTo
	 */
	public Byte getGradeLevelTo() {
		beforeGet();
		return gradeLevelTo;
	}

	/**
	 * @param gradeLevelTo
	 *            the gradeLevelTo to set
	 */
	public void setGradeLevelTo(Byte gradeLevelTo) {
		beforeSet();
		this.gradeLevelTo = gradeLevelTo;
	}

	/**
	 * @return the education
	 */
	public String getEducation() {
		beforeGet();
		return education;
	}

	/**
	 * @param education
	 *            the education to set
	 */
	public void setEducation(String education) {
		beforeSet();
		this.education = education;
	}

	/**
	 * @return the teachYears
	 */
	public Byte getTeachYears() {
		beforeGet();
		return teachYears;
	}

	/**
	 * @param teachYears
	 *            the teachYears to set
	 */
	public void setTeachYears(Byte teachYears) {
		beforeSet();
		this.teachYears = teachYears;
	}

	/**
	 * @return the speciality
	 */
	public List<String> getSpeciality() {
		beforeGet();
		return speciality;
	}

	/**
	 * @return the speciality
	 */
	public List<String> getSpecialityFor() {
		beforeGet();
		if (speciality == null) {
			beforeSet();
			speciality = new ActivatableArrayList<String>();
		}
		return speciality;
	}

	/**
	 * @return the score
	 */
	public Integer getScore() {
		beforeGet();
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(Integer score) {
		beforeSet();
		this.score = score;
	}

	/**
	 * @return the homepage
	 */
	public String getHomepage() {
		beforeGet();
		return homepage;
	}

	/**
	 * @param homepage
	 *            the homepage to set
	 */
	public void setHomepage(String homepage) {
		beforeSet();
		this.homepage = homepage;
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

}
