/*
 * File   Service.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web;

import com.changev.tutor.model.UserModel;

/**
 * <p>
 * 系统服务。
 * </p>
 * 
 * @author ren
 * 
 */
public interface Service<INPUT> {

	/**
	 * <p>
	 * 执行服务处理过程。
	 * </p>
	 * 
	 * <p>
	 * 通常情况下，由客户端发送JSON转换为输入参数，处理结果也转换为JSON发送到客户端。
	 * </p>
	 * 
	 * @param user
	 *            登录用户
	 * @param input
	 *            输入参数
	 * @return 处理结果
	 * @throws Throwable
	 */
	String run(UserModel user, INPUT input) throws Throwable;

}
