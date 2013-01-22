/*
 * File   SessionContainer.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.UserModel;
import com.db4o.ObjectContainer;
import com.db4o.ext.InvalidIDException;

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
		return container;
	}

	/**
	 * <p>
	 * 取得当前会话中保存的容器实例，实例不存在时创建对象。
	 * </p>
	 * 
	 * <p>
	 * 相同于{@link #get(HttpServletRequest, boolean) get(request, true)}。
	 * </p>
	 * 
	 * @param request
	 * @return
	 */
	public static SessionContainer get(HttpServletRequest request) {
		return get(request, true);
	}

	/**
	 * <p>
	 * 取得当前登录用户。
	 * </p>
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends UserModel> T getLoginUser(
			HttpServletRequest request) {
		SessionContainer container = get(request, false);
		return container == null ? null : (T) container.getLoginUser();
	}

	private Long loginUserId;
	private Date loginDateTime;
	private Messages sessionMessage = new Messages();
	private String checkCode;

	private transient UserModel loginUser;

	SessionContainer() {
	}

	/**
	 * <p>
	 * 执行用户登录操作。
	 * </p>
	 * 
	 * @param loginUser
	 */
	public void login(long loginUserId) {
		this.loginUserId = loginUserId;
		this.loginDateTime = Tutor.currentDateTime();
	}

	/**
	 * <p>
	 * 执行用户退出登录操作。
	 * </p>
	 */
	public void logout() {
		if (loginUserId != null) {
			ObjectContainer objc = Tutor.getRootContainer().openSession();
			try {
				UserModel user = objc.ext().getByID(loginUserId);
				if (user != null) {
					user.setLastLoginDateTime(loginDateTime);
					objc.store(user);
					objc.commit();
				}
			} finally {
				objc.close();
				loginUserId = null;
				loginDateTime = null;
				loginUser = null;
			}
		}
	}

	/**
	 * @return the loginUser
	 */
	@SuppressWarnings("unchecked")
	public <T extends UserModel> T getLoginUser() {
		if (loginUserId == null)
			return null;
		if (loginUser == null
				|| !Tutor.getCurrentContainerExt().isStored(loginUser)) {
			try {
				loginUser = Tutor.getCurrentContainerExt().getByID(loginUserId);
			} catch (InvalidIDException e) {
				loginUserId = null;
				loginDateTime = null;
				loginUser = null;
				throw e;
			}
		}
		return (T) loginUser;
	}

	/**
	 * @return the loginUserId
	 */
	public Long getLoginUserId() {
		return loginUserId;
	}

	/**
	 * @return the loginDateTime
	 */
	public Date getLoginDateTime() {
		return loginDateTime;
	}

	/**
	 * @return the sessionMessage
	 */
	public Messages getSessionMessage() {
		return sessionMessage;
	}

	/**
	 * @param sessionMessage
	 *            the sessionMessage to set
	 */
	public void setSessionMessage(Messages sessionMessage) {
		this.sessionMessage = sessionMessage;
	}

	/**
	 * @return the checkCode
	 */
	public String getCheckCode() {
		String code = checkCode;
		checkCode = null;
		return code;
	}

	/**
	 * @param checkCode
	 *            the checkCode to set
	 */
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

}
