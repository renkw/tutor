/*
 * File   View.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ren
 * 
 */
public interface View extends Serializable {

	boolean preRender(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException;

	void postRender(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException;
	
}
