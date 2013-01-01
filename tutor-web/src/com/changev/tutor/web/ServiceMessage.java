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

}
