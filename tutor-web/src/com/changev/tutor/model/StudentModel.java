/*
 * File   StudentModel.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.Date;
import java.util.Map;

import com.db4o.ObjectContainer;
import com.db4o.collections.ActivatableHashMap;
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
	private Map<String, String> defaultAnswerer;
	@Indexed
	private ParentModel parent;

	@Override
	public void objectOnNew(ObjectContainer container) {
		super.objectOnNew(container);
		setRole(UserRole.Student);
	}

	@Override
	public void objectOnUpdate(ObjectContainer container) {
		super.objectOnUpdate(container);
		setRole(UserRole.Student);
	}

	@Override
	public UserContactModel getContact() {
		if (super.getContact() == null && getParent() != null)
			return getParent().getContact();
		return super.getContact();
	}

	@Override
	public StudentModel clone() {
		return (StudentModel) super.clone();
	}

	/**
	 * @return the facePicture
	 */
	public String getFacePicture() {
		beforeGet();
		return facePicture;
	}

	/**
	 * @param facePicture
	 *            the facePicture to set
	 */
	public void setFacePicture(String facePicture) {
		beforeSet();
		this.facePicture = facePicture;
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
	public Date getBirthday() {
		beforeGet();
		return birthday;
	}

	/**
	 * @param birthday
	 *            the birthday to set
	 */
	public void setBirthday(Date birthday) {
		beforeSet();
		this.birthday = birthday;
	}

	/**
	 * @return the school
	 */
	public String getSchool() {
		beforeGet();
		return school;
	}

	/**
	 * @param school
	 *            the school to set
	 */
	public void setSchool(String school) {
		beforeSet();
		this.school = school;
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
	 * @return the hobby
	 */
	public String getHobby() {
		beforeGet();
		return hobby;
	}

	/**
	 * @param hobby
	 *            the hobby to set
	 */
	public void setHobby(String hobby) {
		beforeSet();
		this.hobby = hobby;
	}

	/**
	 * @return the defaultServicer
	 */
	public Map<String, String> getDefaultAnswerer() {
		beforeGet();
		return defaultAnswerer;
	}

	/**
	 * @return the defaultServicer
	 */
	public Map<String, String> getDefaultAnswererFor() {
		beforeGet();
		if (defaultAnswerer == null) {
			beforeSet();
			defaultAnswerer = new ActivatableHashMap<String, String>();
		}
		return defaultAnswerer;
	}

	/**
	 * @return the parent
	 */
	public ParentModel getParent() {
		beforeGet();
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(ParentModel parent) {
		beforeSet();
		this.parent = parent;
	}

}
