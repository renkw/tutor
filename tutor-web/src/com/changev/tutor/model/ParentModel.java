/*
 * File   ParentModel.java
 * Create 2013/01/12
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.changev.tutor.Tutor;
import com.db4o.ObjectContainer;

/**
 * <p>
 * 家长。
 * </p>
 * 
 * @author ren
 * 
 */
public class ParentModel extends UserModel {

	private static final long serialVersionUID = 5756233700028997499L;

	public static final String PROVINCE = "province";
	public static final String CITY = "city";
	public static final String DISTRICT = "district";

	private String province;
	private String city;
	private String district;

	public ParentModel() {
		super.setRole(UserRole.Parent);
	}

	public String getLocation() {
		beforeGet();
		return new StringBuilder().append(StringUtils.defaultString(province))
				.append(StringUtils.defaultString(city))
				.append(StringUtils.defaultString(district)).toString();
	}

	public List<StudentModel> getChildren() {
		return Tutor.getCurrentContainer().queryByExample(
				ModelFactory.getParentStudentExample(getEmail()));
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
	public ParentModel clone() {
		return (ParentModel) super.clone();
	}

	@Override
	public void setRole(UserRole role) {
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		beforeGet();
		return province;
	}

	/**
	 * @param province
	 *            the province to set
	 */
	public void setProvince(String province) {
		beforeSet();
		this.province = province;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		beforeGet();
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		beforeSet();
		this.city = city;
	}

	/**
	 * @return the district
	 */
	public String getDistrict() {
		beforeGet();
		return district;
	}

	/**
	 * @param district
	 *            the district to set
	 */
	public void setDistrict(String district) {
		beforeSet();
		this.district = district;
	}

}
