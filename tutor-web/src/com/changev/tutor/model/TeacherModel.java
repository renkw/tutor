/*
 * File   TeacherModel.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.Date;

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

	// basic
	private Boolean male;
	private Date birthday;
	private String subject;
	private String grade;
	private Integer gradeLevelFrom;
	private Integer gradeLevelTo;
	private String education;
	private Integer teachYears;
	private Integer score;
	private String homepage;

	public void clone(TeacherModel another) {
		super.clone(another);
		this.setMale(getMale());
		this.setBirthday(getBirthday());
		this.setSubject(getSubject());
		this.setGrade(getGrade());
		this.setGradeLevelFrom(getGradeLevelFrom());
		this.setGradeLevelTo(getGradeLevelTo());
		this.setEducation(getEducation());
		this.setTeachYears(getTeachYears());
		this.setScore(getScore());
		this.setHomepage(getHomepage());
	}

	@Override
	public TeacherModel clone() {
		return (TeacherModel) super.clone();
	}

	/**
	 * @return the male
	 */
	public Boolean getMale() {
		return male;
	}

	/**
	 * @param male
	 *            the male to set
	 */
	public void setMale(Boolean male) {
		this.male = male;
	}

	/**
	 * @return the birthday
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * @param birthday
	 *            the birthday to set
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
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
	 * @return the grade
	 */
	public String getGrade() {
		return grade;
	}

	/**
	 * @param grade
	 *            the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}

	/**
	 * @return the gradeLevelFrom
	 */
	public Integer getGradeLevelFrom() {
		return gradeLevelFrom;
	}

	/**
	 * @param gradeLevelFrom
	 *            the gradeLevelFrom to set
	 */
	public void setGradeLevelFrom(Integer gradeLevelFrom) {
		this.gradeLevelFrom = gradeLevelFrom;
	}

	/**
	 * @return the gradeLevelTo
	 */
	public Integer getGradeLevelTo() {
		return gradeLevelTo;
	}

	/**
	 * @param gradeLevelTo
	 *            the gradeLevelTo to set
	 */
	public void setGradeLevelTo(Integer gradeLevelTo) {
		this.gradeLevelTo = gradeLevelTo;
	}

	/**
	 * @return the education
	 */
	public String getEducation() {
		return education;
	}

	/**
	 * @param education
	 *            the education to set
	 */
	public void setEducation(String education) {
		this.education = education;
	}

	/**
	 * @return the teachYears
	 */
	public Integer getTeachYears() {
		return teachYears;
	}

	/**
	 * @param teachYears
	 *            the teachYears to set
	 */
	public void setTeachYears(Integer teachYears) {
		this.teachYears = teachYears;
	}

	/**
	 * @return the score
	 */
	public Integer getScore() {
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(Integer score) {
		this.score = score;
	}

	/**
	 * @return the homepage
	 */
	public String getHomepage() {
		return homepage;
	}

	/**
	 * @param homepage
	 *            the homepage to set
	 */
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

}
