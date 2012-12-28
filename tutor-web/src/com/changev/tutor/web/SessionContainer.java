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
 * <p>
 * 会话上下文容器。
 * </p>
 * 
 * @author ren
 * 
 */
public final class SessionContainer implements Serializable {

	private static final long serialVersionUID = -1701049081787559238L;

	/**
	 * <p>
	 * 取得当前会话中保存的容器实例。
	 * </p>
	 * 
	 * <p>
	 * 如果指定create为true，当会话对象不存在或容器实例不存在时，自动创建。
	 * </p>
	 * 
	 * @param request
	 * @param create
	 * @return
	 */
	public static SessionContainer get(HttpServletRequest request,
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

	/**
	 * <p>
	 * 取得当前会话中保存的容器实例，实例不存在时不会创建对象。
	 * </p>
	 * 
	 * <p>
	 * 相同于{@link #get(HttpServletRequest, boolean) get(request, false)}。
	 * </p>
	 * 
	 * @param request
	 * @return
	 */
	public static SessionContainer get(HttpServletRequest request) {
		return get(request, false);
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
