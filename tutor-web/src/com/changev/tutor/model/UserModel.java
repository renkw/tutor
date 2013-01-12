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
	private UserRole role;
	private UserState state;
	private Date lastLoginDateTime;
	private String secureCode;
	private UserModel parent;
	private UserContactModel contact;

	public void clone(UserModel another) {
		super.clone(another);
		this.setEmail(another.getEmail());
		this.setPassword(another.getPassword());
		this.setName(another.getName());
		this.setRole(another.getRole());
		this.setState(another.getState());
		this.setLastLoginDateTime(another.getLastLoginDateTime());
		this.setSecureCode(another.getSecureCode());
		this.setParent(another.getParent());
		this.setContact(another.getContact());
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
	 * @return the parent
	 */
	public UserModel getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(UserModel parent) {
		this.parent = parent;
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

}
