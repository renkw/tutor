/*
 * File   OrganizationModel.java
 * Create 2013/01/12
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.collections.ActivatableArrayList;

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

	private String logoPicture;
	private List<String> subjects;
	private Integer teacherCount;
	private String homepage;
	private Integer score;
	private Integer accountLevel;

	@Override
	public void objectOnNew(ObjectContainer container) {
		super.objectOnNew(container);
		setRole(UserRole.Organization);
	}

	@Override
	public OrganizationModel clone() {
		return (OrganizationModel) super.clone();
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
	public List<String> getSubjects() {
		beforeGet();
		return subjects;
	}

	/**
	 * @return the subjects
	 */
	public List<String> getSubjectsFor() {
		beforeGet();
		if (subjects == null) {
			beforeSet();
			subjects = new ActivatableArrayList<String>();
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
