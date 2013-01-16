/*
 * File   ServiceMessage.java
 * Create 2012/12/30
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web;

/**
 * <p>
 * 服务消息。
 * </p>
 * 
 * @author ren
 * 
 */
public class ServiceMessage {

	private int code;
	private String description;
	private String result = "";

	public ServiceMessage() {
	}

	public ServiceMessage(int code, String description) {
		this.code = code;
		this.description = description;
	}

	/**
	 * @return the serviceMessage
	 */
	public boolean isServiceMessage() {
		return true;
	}

	/**
	 * @param serviceMessage
	 *            the serviceMessage to set
	 */
	public void setServiceMessage(boolean serviceMessage) {
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * 成功返回
	 */
	public static final int CODE_SUCCESS = 200;
	/**
	 * 系统内部错误
	 */
	public static final int CODE_INTERNAL_ERROR = 505;
}
