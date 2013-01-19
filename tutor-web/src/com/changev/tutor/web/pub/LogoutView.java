/*
 * File   LogoutView.java
 * Create 2013/01/19
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.pub;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.changev.tutor.web.View;

/**
 * <p>
 * 退出登录。
 * </p>
 * 
 * @author ren
 * 
 */
public class LogoutView implements View {

	private static final Logger logger = Logger.getLogger(LogoutView.class);

	private String successPage;

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
		HttpSession session = request.getSession(false);
		if (session != null)
			session.invalidate();
		if (logger.isDebugEnabled())
			logger.debug("[preRender] layout. goto " + successPage);
		response.sendRedirect(request.getContextPath() + successPage);
		return false;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[postRender] called");
	}

	/**
	 * @return the successPage
	 */
	public String getSuccessPage() {
		return successPage;
	}

	/**
	 * @param successPage
	 *            the successPage to set
	 */
	public void setSuccessPage(String successPage) {
		this.successPage = successPage;
	}

}
