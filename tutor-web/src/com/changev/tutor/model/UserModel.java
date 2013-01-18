/*
 * File   UserModel.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.db4o.ObjectContainer;
import com.db4o.config.annotations.Indexed;

/**
 * <p>
 * 用户。
 * </p>
 * 
 * @author ren
 * 
 */
public class UserModel extends AbstractModel {

	private static final long serialVersionUID = -360438261627805753L;

	@Indexed
	private String email;
	private String password;
	private String name;
	@Indexed
	private String province;
	@Indexed
	private String city;
	@Indexed
	private String district;
	private String description;
	private UserRole role;
	private UserState state;
	private Date lastLoginDateTime;
	private String secureCode;
	private UserContactModel contact;
	private UserPrivacy accountPrivacy;
	private UserPrivacy contactPrivacy;

	@Override
	public void objectOnNew(ObjectContainer container) {
		super.objectOnNew(container);
		if (getState() == null)
			setState(UserState.NotActivated);
		if (getSecureCode() == null)
			setSecureCode(ModelFactory.generateSecureCode());
		if (getAccountPrivacy() == null)
			setAccountPrivacy(UserPrivacy.ContacterOnly);
		if (getContactPrivacy() == null)
			setContactPrivacy(UserPrivacy.ContacterOnly);
	}

	@Override
	public UserModel clone() {
		return (UserModel) super.clone();
	}

	public String getLocation() {
		beforeGet();
		return new StringBuilder().append(StringUtils.defaultString(province))
				.append(StringUtils.defaultString(city))
				.append(StringUtils.defaultString(district)).toString();
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		beforeGet();
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		beforeSet();
		this.email = email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		beforeGet();
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		beforeSet();
		this.password = password;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		beforeGet();
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		beforeSet();
		this.name = name;
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

	/**
	 * @return the description
	 */
	public String getDescription() {
		beforeGet();
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		beforeSet();
		this.description = description;
	}

	/**
	 * @return the role
	 */
	public UserRole getRole() {
		beforeGet();
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(UserRole role) {
		beforeSet();
		this.role = role;
	}

	/**
	 * @return the state
	 */
	public UserState getState() {
		beforeGet();
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(UserState state) {
		beforeSet();
		this.state = state;
	}

	/**
	 * @return the lastLoginDateTime
	 */
	public Date getLastLoginDateTime() {
		beforeGet();
		return lastLoginDateTime;
	}

	/**
	 * @param lastLoginDateTime
	 *            the lastLoginDateTime to set
	 */
	public void setLastLoginDateTime(Date lastLoginDateTime) {
		beforeSet();
		this.lastLoginDateTime = lastLoginDateTime;
	}

	/**
	 * @return the secureCode
	 */
	public String getSecureCode() {
		beforeGet();
		return secureCode;
	}

	/**
	 * @param secureCode
	 *            the secureCode to set
	 */
	public void setSecureCode(String secureCode) {
		beforeSet();
		this.secureCode = secureCode;
	}

	/**
	 * @return the contact
	 */
	public UserContactModel getContact() {
		beforeGet();
		return contact;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(UserContactModel contact) {
		beforeSet();
		this.contact = contact;
	}

	/**
	 * @return the accountPrivacy
	 */
	public UserPrivacy getAccountPrivacy() {
		beforeGet();
		return accountPrivacy;
	}

	/**
	 * @param accountPrivacy
	 *            the accountPrivacy to set
	 */
	public void setAccountPrivacy(UserPrivacy accountPrivacy) {
		beforeSet();
		this.accountPrivacy = accountPrivacy;
	}

	/**
	 * @return the contactPrivacy
	 */
	public UserPrivacy getContactPrivacy() {
		beforeGet();
		return contactPrivacy;
	}

	/**
	 * @param contactPrivacy
	 *            the contactPrivacy to set
	 */
	public void setContactPrivacy(UserPrivacy contactPrivacy) {
		beforeSet();
		this.contactPrivacy = contactPrivacy;
	}

}
