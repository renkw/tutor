/*
 * File   AdminModel.java
 * Create 2013/01/12
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import com.db4o.ObjectContainer;

/**
 * <p>
 * 管理员。
 * </p>
 * 
 * @author ren
 * 
 */
public class AdminModel extends UserModel {

	private static final long serialVersionUID = 5756233700028997499L;

	public AdminModel() {
		super.setRole(UserRole.Admin);
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
	public AdminModel clone() {
		return (AdminModel) super.clone();
	}

	@Override
	public void setRole(UserRole role) {
	}

}
