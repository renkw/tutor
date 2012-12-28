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
 * 用户模型。
 * </p>
 * 
 * @author ren
 * 
 */
public class UserModel extends AbstractModel {

	private static final long serialVersionUID = -360438261627805753L;

	@Indexed
	private String username;
	private String password;
	private String tel;
	private String email;
	private UserRole role;
	private Date loginDateTime;
	private String securityCode;
	private UserModel parentUser;

	public UserModel() {
	}

	public UserModel(UserModel copy) {
		super(copy);
		this.setUsername(copy.getUsername());
		this.setPassword(copy.getPassword());
		this.setTel(copy.getTel());
		this.setEmail(copy.getEmail());
		this.setRole(copy.getRole());
		this.setLoginDateTime(copy.getLoginDateTime());
		this.setSecurityCode(copy.getSecurityCode());
		this.setParentUser(copy.getParentUser());
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
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
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * @param tel
	 *            the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
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
	 * @return the loginDateTime
	 */
	public Date getLoginDateTime() {
		return loginDateTime;
	}

	/**
	 * @param loginDateTime
	 *            the loginDateTime to set
	 */
	public void setLoginDateTime(Date loginDateTime) {
		this.loginDateTime = loginDateTime;
	}

	/**
	 * @return the securityCode
	 */
	public String getSecurityCode() {
		return securityCode;
	}

	/**
	 * @param securityCode
	 *            the securityCode to set
	 */
	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	/**
	 * @return the parentUser
	 */
	public UserModel getParentUser() {
		return parentUser;
	}

	/**
	 * @param parentUser
	 *            the parentUser to set
	 */
	public void setParentUser(UserModel parentUser) {
		this.parentUser = parentUser;
	}

}
