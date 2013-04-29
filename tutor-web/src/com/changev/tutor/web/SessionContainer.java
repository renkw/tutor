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
import com.changev.tutor.model.QuestionModel;
import com.changev.tutor.model.UserModel;
import com.changev.tutor.util.PageList;
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
	public static UserModel getLoginUser(HttpServletRequest request) {
		SessionContainer container = get(request, false);
		return container == null ? null : container.getLoginUser();
	}

	private Long loginUserId;
	private String loginUserId_str;
	private Date loginDateTime;
	private String checkCode;
	private String systemMessage;
	private String actionMessage;
	private transient PageList<QuestionModel> questionList;
	private String questionListQuery;

	SessionContainer() {
	}

	/**
	 * <p>
	 * 执行用户登录操作。
	 * </p>
	 * 
	 * @param loginUser
	 */
	public void login(long loginUserId, String loginUserId_str) {
		this.loginUserId = loginUserId;
		this.loginUserId_str = loginUserId_str;
		this.loginDateTime = Tutor.currentDateTime();
	}
	
	public String getLoginUserId_str() {
		return loginUserId_str;
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
			}
		}
	}

	/**
	 * @return the loginUser
	 */
	public UserModel getLoginUser() {
		if (loginUserId == null)
			return null;
		try {
			return Tutor.getCurrentContainerExt().getByID(loginUserId);
		} catch (InvalidIDException e) {
			loginUserId = null;
			loginDateTime = null;
			throw e;
		}
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
	 * @return the systemMessage
	 */
	public String getSystemMessage() {
		return systemMessage;
	}

	/**
	 * @param systemMessage
	 *            the systemMessage to set
	 */
	public void setSystemMessage(String systemMessage) {
		this.systemMessage = systemMessage;
	}

	/**
	 * @return the actionMessage
	 */
	public String getActionMessage() {
		return actionMessage;
	}

	/**
	 * @param actionMessage
	 *            the actionMessage to set
	 */
	public void setActionMessage(String actionMessage) {
		this.actionMessage = actionMessage;
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

	/**
	 * @return the questionList
	 */
	public PageList<QuestionModel> getQuestionList() {
		return questionList;
	}

	/**
	 * @param questionList
	 *            the questionList to set
	 */
	public void setQuestionList(PageList<QuestionModel> questionList) {
		this.questionList = questionList;
	}

	/**
	 * @return the questionListQuery
	 */
	public String getQuestionListQuery() {
		return questionListQuery;
	}

	/**
	 * @param questionListQuery
	 *            the questionListQuery to set
	 */
	public void setQuestionListQuery(String questionListQuery) {
		this.questionListQuery = questionListQuery;
	}

}
