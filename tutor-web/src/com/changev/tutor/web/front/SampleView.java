/*
 * File   SampleView.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.front;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.changev.tutor.web.View;

/**
 * @author ren
 * 
 */
public class SampleView implements View {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(SampleView.class);

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
		request.setAttribute("user", "Foo");
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (logger.isTraceEnabled())
			logger.trace("[postRender] called");
	}

}
