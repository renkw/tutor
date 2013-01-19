/*
 * File   ParamUserValidator.java
 * Create 2013/01/19
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.util;

import org.apache.commons.lang.StringUtils;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.ModelFactory;
import com.changev.tutor.model.UserRole;

/**
 * <p>
 * 验证用户是否存在。
 * </p>
 * 
 * @author ren
 * 
 */
public class ParamUserValidator extends ParamValidator {

	private UserRole role;

	public ParamUserValidator() {
	}

	public ParamUserValidator(UserRole role) {
		this.role = role;
	}

	@Override
	protected boolean validate(String v) {
		return StringUtils.isEmpty(v)
				|| Tutor.getCurrentContainer()
						.queryByExample(ModelFactory.getUserExample(v, role))
						.hasNext();
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

}
