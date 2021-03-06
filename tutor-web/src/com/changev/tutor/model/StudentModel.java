/*
 * File   StudentModel.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.changev.tutor.Tutor;
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

	public static final String FACE_PICTURE = "facePicture";
	public static final String MALE = "male";
	public static final String BIRTHDAY = "birthday";
	public static final String SCHOOL = "school";
	public static final String GRADE = "grade";
	public static final String GRADE_LEVEL = "gradeLevel";
	public static final String HOBBY = "hobby";
	public static final String DEFAULT_ANSWERER = "defaultAnswerer";
	public static final String PARENT = "parent";

	private String facePicture;
	private Boolean male;
	private String birthday;
	private String school;
	private String grade;
	private Byte gradeLevel;
	private String hobby;
	private Map<String, UserModel> defaultAnswerer;
	@Indexed
	private ParentModel parent;

	public StudentModel() {
		super.setRole(UserRole.Student);
	}

	public List<QuestionModel> getQuestions() {
		return Tutor.getCurrentContainer().queryByExample(
				ModelFactory.getUserQuestionExample(getEmail()));
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

	@Override
	public void setRole(UserRole role) {
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
	public Map<String, UserModel> getDefaultAnswerer() {
		beforeGet();
		if (defaultAnswerer == null)
			return Collections.emptyMap();
		return defaultAnswerer;
	}

	/**
	 * @return the defaultServicer
	 */
	public Map<String, UserModel> getDefaultAnswererFor() {
		beforeGet();
		if (defaultAnswerer == null) {
			beforeSet();
			defaultAnswerer = new ActivatableHashMap<String, UserModel>();
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
