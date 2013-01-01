/*
 * File   ModelFactory.java
 * Create 2013/01/01
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

/**
 * @author ren
 * 
 */
public final class ModelFactory {

	public static UserModel getUserExample(String username) {
		UserModel model = new UserModel();
		model.setUsername(username);
		model.setDeleted(Boolean.FALSE);
		return model;
	}

	public static UserModel getUserExample(String username, String password) {
		UserModel model = new UserModel();
		model.setUsername(username);
		model.setPassword(password);
		model.setDeleted(Boolean.FALSE);
		return model;
	}

}
