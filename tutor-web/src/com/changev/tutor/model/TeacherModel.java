/*
 * File   TeacherModel.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.sql.Date;

/**
 * <p>
 * 教师模型。
 * </p>
 * 
 * @author ren
 * 
 */
public class TeacherModel extends UserModel {

	private static final long serialVersionUID = -5724580114977645750L;

	private String realName;
	private Boolean male;
	private Date birthday;
	private String subject;
	private Integer score;
	private String description;

	public TeacherModel() {
	}

	public TeacherModel(TeacherModel copy) {
		super(copy);
		this.setRealName(copy.getRealName());
		this.setMale(copy.getMale());
		this.setBirthday(copy.getBirthday());
		this.setSubject(copy.getSubject());
		this.setScore(copy.getScore());
		this.setDescription(copy.getDescription());
	}

	/**
	 * @return the realName
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * @param realName
	 *            the realName to set
	 */
	public void setRealName(String realName) {
		this.realName = realName;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
