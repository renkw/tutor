/*
 * File   StudentModel.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.List;
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
	private String birthday;
	private String school;
	private String grade;
	private Byte gradeLevel;
	private String hobby;
	private Map<String, String> defaultAnswerer;
	@Indexed
	private ParentModel parent;

	public StudentModel() {
		super.setRole(UserRole.Student);
	}

	@Override
	public void setRole(UserRole role) {
	}

	@Override
	public UserContactModel getContact() {
		if (super.getContact() == null && getParent() != null)
			return getParent().getContact();
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
	public StudentModel clone() {
		return (StudentModel) super.clone();
	}

	public List<QuestionModel> getQuestions() {
		// TODO
		throw new UnsupportedOperationException();
	}

	public List<QuestionModel> getUnclosedQuestions() {
		// TODO
		throw new UnsupportedOperationException();
	}

	public List<QuestionModel> getClosedQuestions() {
		// TODO
		throw new UnsupportedOperationException();
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
