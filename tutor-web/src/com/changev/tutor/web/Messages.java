/*
 * File   Messages.java
 * Create 2012/12/28
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.changev.tutor.Tutor;

/**
 * <p>
 * 页面处理消息。
 * </p>
 * 
 * @author ren
 * 
 */
public class Messages implements Serializable {

	private static final long serialVersionUID = -6296696698861706716L;

	/**
	 * <p>
	 * 取得HttpServletRequest中的消息实例，如果不存在则创建。
	 * </p>
	 * 
	 * @param request
	 * @return
	 */
	public static Messages get(HttpServletRequest request, boolean create) {
		Messages msg = (Messages) request.getAttribute(Tutor.KEY_MESSAGES);
		if (msg == null && create) {
			msg = new Messages();
			request.setAttribute(Tutor.KEY_MESSAGES, msg);
		}
		return msg;
	}

	/**
	 * <p>
	 * 取得HttpServletRequest中的消息实例，如果不存在则创建。
	 * </p>
	 * 
	 * <p>
	 * 相同于{@link #get(HttpServletRequest, boolean) get(request, true)}
	 * </p>
	 * 
	 * @param request
	 * @return
	 */
	public static Messages get(HttpServletRequest request) {
		Messages msg = (Messages) request.getAttribute(Tutor.KEY_MESSAGES);
		if (msg == null) {
			msg = new Messages();
			request.setAttribute(Tutor.KEY_MESSAGES, msg);
		}
		return msg;
	}

	/**
	 * <p>
	 * 添加一条错误消息到HttpServletRequest中。
	 * </p>
	 * 
	 * @param request
	 * @param name
	 * @param message
	 * @return
	 */
	public static Messages addError(HttpServletRequest request, String name,
			String message) {
		return get(request).addError(name, message);
	}

	/**
	 * <p>
	 * 查看HttpServletRequest中是否存在错误消息。
	 * </p>
	 * 
	 * @param request
	 * @return
	 */
	public static boolean hasErrors(HttpServletRequest request) {
		Messages msg = (Messages) request.getAttribute("msg");
		return msg != null && msg.hasErrors();
	}

	private Map<String, String> messages = Collections.emptyMap();
	private Map<String, String> warnings = Collections.emptyMap();
	private Map<String, String> errors = Collections.emptyMap();

	/**
	 * <p>
	 * 查看是否存在任何类型消息。
	 * </p>
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return messages.isEmpty() && warnings.isEmpty() && errors.isEmpty();
	}

	/**
	 * <p>
	 * 删除所有消息。
	 * </p>
	 */
	public void clearMessages() {
		messages.clear();
		warnings.clear();
		errors.clear();
	}

	public Messages addMessage(String name, String message) {
		if (messages == Collections.EMPTY_MAP)
			messages = new LinkedHashMap<String, String>();
		messages.put(name, message);
		return this;
	}

	public Messages addWarning(String name, String message) {
		if (warnings == Collections.EMPTY_MAP)
			warnings = new LinkedHashMap<String, String>();
		warnings.put(name, message);
		return this;
	}

	public Messages addError(String name, String message) {
		if (errors == Collections.EMPTY_MAP)
			errors = new LinkedHashMap<String, String>();
		errors.put(name, message);
		return this;
	}

	/**
	 * <p>
	 * 查看是否存在一般消息。
	 * </p>
	 * 
	 * @return
	 */
	public boolean hasMessages() {
		return !messages.isEmpty();
	}

	/**
	 * <p>
	 * 查看是否存在警告消息。
	 * </p>
	 * 
	 * @return
	 */
	public boolean hasWarnings() {
		return !warnings.isEmpty();
	}

	/**
	 * <p>
	 * 查看是否存在错误消息。
	 * </p>
	 * 
	 * @return
	 */
	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	/**
	 * @return the messages
	 */
	public Map<String, String> getMessages() {
		return messages;
	}

	/**
	 * @return the warnings
	 */
	public Map<String, String> getWarnings() {
		return warnings;
	}

	/**
	 * @return the errors
	 */
	public Map<String, String> getErrors() {
		return errors;
	}

}
