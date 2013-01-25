/*
 * File   TeacherModel.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.List;
import java.util.Set;

import com.changev.tutor.Tutor;
import com.db4o.ObjectContainer;
import com.db4o.collections.ActivatableHashSet;
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

	public static final String MALE = "male";
	public static final String BIRTHDAY = "birthday";
	public static final String SUBJECTS = "subjects";
	public static final String GRADE = "grade";
	public static final String GRADE_LEVEL_FROM = "gradeLevelFrom";
	public static final String GRADE_LEVEL_TO = "gradeLevelTo";
	public static final String EDUCATION = "education";
	public static final String TEACHED_YEARS = "teachedYears";
	public static final String SPECIALITY = "speciality";
	public static final String HOMEPAGE = "homepage";
	public static final String CONTACTERS = "contacters";
	public static final String ORGANIZATION = "organization";
	public static final String SCORE = "score";

	private Boolean male;
	private String birthday;
	@Indexed
	private Set<String> subjects;
	private String grade;
	private Byte gradeLevelFrom;
	private Byte gradeLevelTo;
	private String education;
	private Byte teachedYears;
	private Set<String> speciality;
	private String homepage;
	private Set<UserModel> contacters;
	@Indexed
	private OrganizationModel organization;
	private Integer score;

	public TeacherModel() {
		super.setRole(UserRole.Teacher);
	}

	public String getFullName() {
		return new StringBuilder().append(getOrganization().getName())
				.append(' ').append(getName()).toString();
	}

	public List<QuestionModel> getAssignedQuestions() {
		return Tutor.getCurrentContainer().queryByExample(
				ModelFactory.getAssignedQuestionExample(getEmail()));
	}

	public List<AnswerModel> getAnswers() {
		return Tutor.getCurrentContainer().queryByExample(
				ModelFactory.getTeacherAnswerExample(getEmail()));
	}

	public List<AnswerModel> getAcceptAnswers() {
		return Tutor.getCurrentContainer().queryByExample(
				ModelFactory.getAcceptedAnswerExample(getEmail()));
	}

	@Override
	public void objectOnActivate(ObjectContainer container) {
		super.objectOnActivate(container);
	}

	@Override
	public void objectOnNew(ObjectContainer container) {
		super.objectOnNew(container);
		setScore(Integer.valueOf(0));
	}

	@Override
	public void objectOnUpdate(ObjectContainer container) {
		super.objectOnUpdate(container);
	}

	@Override
	public TeacherModel clone() {
		return (TeacherModel) super.clone();
	}

	@Override
	public void setRole(UserRole role) {
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
	 * @return the subjects
	 */
	public Set<String> getSubjects() {
		beforeGet();
		return subjects;
	}

	/**
	 * @return the subjects
	 */
	public Set<String> getSubjectsFor() {
		beforeGet();
		if (subjects == null) {
			beforeSet();
			subjects = new ActivatableHashSet<String>();
		}
		return subjects;
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
	public Byte getTeachedYears() {
		beforeGet();
		return teachedYears;
	}

	/**
	 * @param teachYears
	 *            the teachYears to set
	 */
	public void setTeachedYears(Byte teachYears) {
		beforeSet();
		this.teachedYears = teachYears;
	}

	/**
	 * @return the speciality
	 */
	public Set<String> getSpeciality() {
		beforeGet();
		return speciality;
	}

	/**
	 * @return the speciality
	 */
	public Set<String> getSpecialityFor() {
		beforeGet();
		if (speciality == null) {
			beforeSet();
			speciality = new ActivatableHashSet<String>();
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
	 * @return the contacters
	 */
	public Set<UserModel> getContacters() {
		beforeGet();
		return contacters;
	}

	/**
	 * @return the contacters
	 */
	public Set<UserModel> getContactersFor() {
		beforeGet();
		if (contacters == null) {
			beforeSet();
			contacters = new ActivatableHashSet<UserModel>();
		}
		return contacters;
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
