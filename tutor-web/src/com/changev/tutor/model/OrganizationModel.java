/*
 * File   OrganizationModel.java
 * Create 2013/01/12
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.List;

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

	public void clone(OrganizationModel another) {
		super.clone(another);
		this.setLogoPicture(another.getLogoPicture());
		this.setSubjects(another.getSubjects());
		this.setTeacherCount(another.getTeacherCount());
		this.setHomepage(another.getHomepage());
		this.setScore(another.getScore());
		this.setAccountLevel(another.getAccountLevel());
	}

	@Override
	public OrganizationModel clone() {
		return (OrganizationModel) super.clone();
	}

	/**
	 * @return the logoPicture
	 */
	public String getLogoPicture() {
		return logoPicture;
	}

	/**
	 * @param logoPicture
	 *            the logoPicture to set
	 */
	public void setLogoPicture(String logoPicture) {
		this.logoPicture = logoPicture;
	}

	/**
	 * @return the subjects
	 */
	public List<String> getSubjects() {
		return subjects;
	}

	/**
	 * @param subjects
	 *            the subjects to set
	 */
	public void setSubjects(List<String> subjects) {
		this.subjects = subjects;
	}

	/**
	 * @return the teacherCount
	 */
	public Integer getTeacherCount() {
		return teacherCount;
	}

	/**
	 * @param teacherCount
	 *            the teacherCount to set
	 */
	public void setTeacherCount(Integer teacherCount) {
		this.teacherCount = teacherCount;
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
	 * @return the accountLevel
	 */
	public Integer getAccountLevel() {
		return accountLevel;
	}

	/**
	 * @param accountLevel
	 *            the accountLevel to set
	 */
	public void setAccountLevel(Integer accountLevel) {
		this.accountLevel = accountLevel;
	}

}
