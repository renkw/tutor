/*
 * File   Service.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;

import com.changev.tutor.model.UserModel;

/**
 * @author ren
 * 
 */
public interface Service extends Serializable {

	Object run(UserModel user, Object input) throws ServletException, IOException;

}
