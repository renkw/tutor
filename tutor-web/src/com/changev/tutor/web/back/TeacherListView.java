/*
 * File   TeacherListView.java
 * Create 2013/01/29
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.back;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.changev.tutor.model.OrganizationModel;
import com.changev.tutor.web.SessionContainer;
import com.changev.tutor.web.View;

/**
 * @author ren
 * 
 */
public class TeacherListView implements View {

	private static final Logger logger = Logger
			.getLogger(TeacherListView.class);

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
		request.setAttribute("teachers", ((OrganizationModel) SessionContainer
				.getLoginUser(request)).getTeachers());
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[postRender] called");
		SessionContainer.get(request).setActionMessage(null);
	}

}
