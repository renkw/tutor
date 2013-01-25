/*
 * File   OrganizationModel.java
 * Create 2013/01/12
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.changev.tutor.Tutor;
import com.db4o.ObjectContainer;
import com.db4o.collections.ActivatableHashSet;

/**
 * <p>
 * 辅导机构。
 * </p>
 * 
 * @author ren
 * 
 */
public class OrganizationModel extends UserModel {

	private static final long serialVersionUID = -8527838031223751954L;

	public static final String LOGO_PICTURE = "logoPicture";
	public static final String SUBJECTS = "subjects";
	public static final String HOMEPAGE = "homepage";
	public static final String TEACHER_COUNT = "teacherCount";
	public static final String SCORE = "score";
	public static final String ACCOUNT_LEVEL = "accountLevel";

	private String logoPicture;
	private Set<String> subjects;
	private String homepage;
	private Integer teacherCount;
	private Integer score;
	private Integer accountLevel;

	public OrganizationModel() {
		super.setRole(UserRole.Organization);
	}

	public List<TeacherModel> getTeachers() {
		return Tutor.getCurrentContainer().queryByExample(
				ModelFactory.getOrganizationTeacherExample(getEmail()));
	}

	public Set<UserModel> getContacters() {
		Set<UserModel> contacters = new HashSet<UserModel>();
		for (TeacherModel teacher : getTeachers())
			contacters.addAll(teacher.getContacters());
		return contacters;
	}

	@Override
	public void objectOnActivate(ObjectContainer container) {
		super.objectOnActivate(container);
	}

	@Override
	public void objectOnNew(ObjectContainer container) {
		super.objectOnNew(container);
		setTeacherCount(Integer.valueOf(0));
		setScore(Integer.valueOf(0));
		setAccountLevel(Integer.valueOf(0));
	}

	@Override
	public void objectOnUpdate(ObjectContainer container) {
		super.objectOnUpdate(container);
	}

	@Override
	public OrganizationModel clone() {
		return (OrganizationModel) super.clone();
	}

	@Override
	public void setRole(UserRole role) {
	}

	/**
	 * @return the logoPicture
	 */
	public String getLogoPicture() {
		beforeGet();
		return logoPicture;
	}

	/**
	 * @param logoPicture
	 *            the logoPicture to set
	 */
	public void setLogoPicture(String logoPicture) {
		beforeSet();
		this.logoPicture = logoPicture;
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
	 * @return the teacherCount
	 */
	public Integer getTeacherCount() {
		beforeGet();
		return teacherCount;
	}

	/**
	 * @param teacherCount
	 *            the teacherCount to set
	 */
	public void setTeacherCount(Integer teacherCount) {
		beforeSet();
		this.teacherCount = teacherCount;
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
	 * @return the accountLevel
	 */
	public Integer getAccountLevel() {
		beforeGet();
		return accountLevel;
	}

	/**
	 * @param accountLevel
	 *            the accountLevel to set
	 */
	public void setAccountLevel(Integer accountLevel) {
		beforeSet();
		this.accountLevel = accountLevel;
	}

}
