/*
 * File   ParentModel.java
 * Create 2013/01/12
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

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

	public void clone(ParentModel another) {
		super.clone(another);
	}

	@Override
	public ParentModel clone() {
		return (ParentModel) super.clone();
	}

}
