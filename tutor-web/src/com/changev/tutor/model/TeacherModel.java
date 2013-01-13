/*
 * File   TeacherModel.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.Date;
import java.util.List;

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
	private Date birthday;
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
		this.setSpeciality(another.getSpeciality());
		this.setScore(getScore());
		this.setHomepage(getHomepage());
		this.setOrganization(another.getOrganization());
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
	public Byte getGradeLevelFrom() {
		return gradeLevelFrom;
	}

	/**
	 * @param gradeLevelFrom
	 *            the gradeLevelFrom to set
	 */
	public void setGradeLevelFrom(Byte gradeLevelFrom) {
		this.gradeLevelFrom = gradeLevelFrom;
	}

	/**
	 * @return the gradeLevelTo
	 */
	public Byte getGradeLevelTo() {
		return gradeLevelTo;
	}

	/**
	 * @param gradeLevelTo
	 *            the gradeLevelTo to set
	 */
	public void setGradeLevelTo(Byte gradeLevelTo) {
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
	public Byte getTeachYears() {
		return teachYears;
	}

	/**
	 * @param teachYears
	 *            the teachYears to set
	 */
	public void setTeachYears(Byte teachYears) {
		this.teachYears = teachYears;
	}

	/**
	 * @return the speciality
	 */
	public List<String> getSpeciality() {
		return speciality;
	}

	/**
	 * @param speciality
	 *            the speciality to set
	 */
	public void setSpeciality(List<String> speciality) {
		this.speciality = speciality;
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

}
