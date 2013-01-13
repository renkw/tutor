/*
 * File   UserModel.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.Date;

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
	private UserRole role;
	private UserState state;
	private Date lastLoginDateTime;
	private String secureCode;
	private UserContactModel contact;
	private UserPrivacy accountPrivacy;
	private UserPrivacy contactPrivacy;

	public void clone(UserModel another) {
		super.clone(another);
		this.setEmail(another.getEmail());
		this.setPassword(another.getPassword());
		this.setName(another.getName());
		this.setProvince(another.getProvince());
		this.setCity(another.getCity());
		this.setDistrict(another.getDistrict());
		this.setRole(another.getRole());
		this.setState(another.getState());
		this.setLastLoginDateTime(another.getLastLoginDateTime());
		this.setSecureCode(another.getSecureCode());
		this.setContact(another.getContact());
		this.setAccountPrivacy(another.getAccountPrivacy());
		this.setContactPrivacy(another.getContactPrivacy());
	}

	@Override
	public UserModel clone() {
		return (UserModel) super.clone();
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @param province
	 *            the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * @param district
	 *            the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	/**
	 * @return the role
	 */
	public UserRole getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(UserRole role) {
		this.role = role;
	}

	/**
	 * @return the state
	 */
	public UserState getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(UserState state) {
		this.state = state;
	}

	/**
	 * @return the lastLoginDateTime
	 */
	public Date getLastLoginDateTime() {
		return lastLoginDateTime;
	}

	/**
	 * @param lastLoginDateTime
	 *            the lastLoginDateTime to set
	 */
	public void setLastLoginDateTime(Date lastLoginDateTime) {
		this.lastLoginDateTime = lastLoginDateTime;
	}

	/**
	 * @return the secureCode
	 */
	public String getSecureCode() {
		return secureCode;
	}

	/**
	 * @param secureCode
	 *            the secureCode to set
	 */
	public void setSecureCode(String secureCode) {
		this.secureCode = secureCode;
	}

	/**
	 * @return the contact
	 */
	public UserContactModel getContact() {
		return contact;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(UserContactModel contact) {
		this.contact = contact;
	}

	/**
	 * @return the accountPrivacy
	 */
	public UserPrivacy getAccountPrivacy() {
		return accountPrivacy;
	}

	/**
	 * @param accountPrivacy
	 *            the accountPrivacy to set
	 */
	public void setAccountPrivacy(UserPrivacy accountPrivacy) {
		this.accountPrivacy = accountPrivacy;
	}

	/**
	 * @return the contactPrivacy
	 */
	public UserPrivacy getContactPrivacy() {
		return contactPrivacy;
	}

	/**
	 * @param contactPrivacy
	 *            the contactPrivacy to set
	 */
	public void setContactPrivacy(UserPrivacy contactPrivacy) {
		this.contactPrivacy = contactPrivacy;
	}

}
