/*
 * File   StudentModel.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.Date;

/**
 * <p>
 * 学生模型。
 * </p>
 * 
 * @author ren
 * 
 */
public class StudentModel extends UserModel {

	private static final long serialVersionUID = 7524370787816259509L;

	private String realName;
	private Boolean male;
	private Date birthday;
	private String schoolName;
	private String grade;
	private Integer level;
	private String hobby;
	private String description;

	public StudentModel() {
	}

	public StudentModel(StudentModel copy) {
		super(copy);
		this.setRealName(copy.getRealName());
		this.setMale(copy.getMale());
		this.setBirthday(copy.getBirthday());
		this.setSchoolName(copy.getSchoolName());
		this.setGrade(copy.getGrade());
		this.setHobby(copy.getHobby());
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
	 * @return the schoolName
	 */
	public String getSchoolName() {
		return schoolName;
	}

	/**
	 * @param schoolName
	 *            the schoolName to set
	 */
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
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
	 * @return the level
	 */
	public Integer getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
	 * @return the hobby
	 */
	public String getHobby() {
		return hobby;
	}

	/**
	 * @param hobby
	 *            the hobby to set
	 */
	public void setHobby(String hobby) {
		this.hobby = hobby;
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
