/*
 * File   SessionContainer.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.UserModel;

/**
 * @author ren
 * 
 */
public final class SessionContainer implements Serializable {

	private static final long serialVersionUID = -1701049081787559238L;

	public static SessionContainer getInstance(HttpServletRequest request,
			boolean create) {
		SessionContainer container = null;
		HttpSession session = request.getSession(create);
		if (session != null)
			container = (SessionContainer) session
					.getAttribute(Tutor.KEY_SESSION_CONTAINER);
		if (container == null && create) {
			container = new SessionContainer();
			session.setAttribute(Tutor.KEY_SESSION_CONTAINER, container);
		}
		return container;
	}

	public static SessionContainer getInstance(HttpServletRequest request) {
		return getInstance(request, false);
	}
	
	private UserModel loginUser;

	private SessionContainer() {
	}

	/**
	 * @return the loginUser
	 */
	public UserModel getLoginUser() {
		return loginUser;
	}

	/**
	 * @param loginUser
	 *            the loginUser to set
	 */
	public void setLoginUser(UserModel loginUser) {
		this.loginUser = loginUser;
	}

}
