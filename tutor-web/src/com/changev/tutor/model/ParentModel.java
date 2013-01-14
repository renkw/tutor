/*
 * File   ParentModel.java
 * Create 2013/01/12
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

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

	@Override
	public void objectOnNew(ObjectContainer container) {
		super.objectOnNew(container);
		setRole(UserRole.Parent);
	}

	@Override
	public ParentModel clone() {
		return (ParentModel) super.clone();
	}

}
