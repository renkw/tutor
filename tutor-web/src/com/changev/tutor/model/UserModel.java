/*
 * File   UserModel.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import com.db4o.ObjectContainer;
import com.db4o.collections.ActivatableHashSet;
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

	public static final String EMAIL = "email";
	public static final String PASSWORD = "password";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String ROLE = "role";
	public static final String STATE = "state";
	public static final String LAST_LOGIN_DATE_TIME = "lastLoginDateTime";
	public static final String SECURE_CODE = "secureCode";
	public static final String CONTACT = "contact";
	public static final String CONTACTERS = "contacters";
	public static final String ACCOUNT_PRIVACY = "accountPrivacy";
	public static final String CONTACT_PRIVACY = "contactPrivacy";

	@Indexed
	private String email;
	private String password;
	private String name;
	private String description;
	private UserRole role;
	private UserState state;
	private Date lastLoginDateTime;
	private String secureCode;
	private UserContactModel contact;
	private Set<UserModel> contacters;
	private UserPrivacy accountPrivacy;
	private UserPrivacy contactPrivacy;

	@SuppressWarnings("unchecked")
	public <T extends UserModel> T as(UserRole role) {
		return role == this.role ? (T) this : null;
	}

	@Override
	public void objectOnActivate(ObjectContainer container) {
		super.objectOnActivate(container);
	}

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
	public void objectOnUpdate(ObjectContainer container) {
		super.objectOnUpdate(container);
	}

	@Override
	public UserModel clone() {
		return (UserModel) super.clone();
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
	 * @return the contacters
	 */
	public Set<UserModel> getContacters() {
		beforeGet();
		if (contacters == null)
			return Collections.emptySet();
		return contacters;
	}

	/**
	 * @return the contacters
	 */
	public Set<UserModel> getContactersFor() {
		beforeGet();
		if (contacters == null) {
			beforeSet();
			contacters = new ActivatableHashSet<UserModel>();
		}
		return contacters;
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
