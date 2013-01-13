/*
 * File   StudentModel.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.Date;
import java.util.Map;

import com.db4o.config.annotations.Indexed;

/**
 * <p>
 * 学生。
 * </p>
 * 
 * @author ren
 * 
 */
public class StudentModel extends UserModel {

	private static final long serialVersionUID = 7524370787816259509L;

	private String facePicture;
	private Boolean male;
	private Date birthday;
	private String school;
	private String grade;
	private Byte gradeLevel;
	private String hobby;
	private Map<String, String> defaultServicer;
	@Indexed
	private ParentModel parent;

	public void clone(StudentModel another) {
		super.clone(another);
		this.setFacePicture(another.getFacePicture());
		this.setMale(another.getMale());
		this.setBirthday(another.getBirthday());
		this.setSchool(another.getSchool());
		this.setGrade(another.getGrade());
		this.setGradeLevel(another.getGradeLevel());
		this.setHobby(another.getHobby());
		this.setDefaultServicer(another.getDefaultServicer());
		this.setParent(another.getParent());
	}

	@Override
	public StudentModel clone() {
		return (StudentModel) super.clone();
	}

	/**
	 * @return the facePicture
	 */
	public String getFacePicture() {
		return facePicture;
	}

	/**
	 * @param facePicture
	 *            the facePicture to set
	 */
	public void setFacePicture(String facePicture) {
		this.facePicture = facePicture;
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
	 * @return the school
	 */
	public String getSchool() {
		return school;
	}

	/**
	 * @param school
	 *            the school to set
	 */
	public void setSchool(String school) {
		this.school = school;
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
	 * @return the gradeLevel
	 */
	public Byte getGradeLevel() {
		return gradeLevel;
	}

	/**
	 * @param gradeLevel
	 *            the gradeLevel to set
	 */
	public void setGradeLevel(Byte gradeLevel) {
		this.gradeLevel = gradeLevel;
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
	 * @return the defaultServicer
	 */
	public Map<String, String> getDefaultServicer() {
		return defaultServicer;
	}

	/**
	 * @param defaultServicer
	 *            the defaultServicer to set
	 */
	public void setDefaultServicer(Map<String, String> defaultServicer) {
		this.defaultServicer = defaultServicer;
	}

	/**
	 * @return the parent
	 */
	public ParentModel getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(ParentModel parent) {
		this.parent = parent;
	}

}
