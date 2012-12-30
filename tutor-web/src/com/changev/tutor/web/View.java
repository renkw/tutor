/*
 * File   View.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 页面视图接口。
 * </p>
 * 
 * @author ren
 * 
 */
public interface View {

	/**
	 * <p>
	 * 模板输出前处理。
	 * </p>
	 * 
	 * <p>
	 * 典型实现为在request或session中设置模板中使用的业务对象。
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @return true表示可以继续执行模板输出。
	 * @throws Throwable
	 */
	boolean preRender(HttpServletRequest request, HttpServletResponse response)
			throws Throwable;

	/**
	 * <p>
	 * 模板输出后处理。
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @throws Throwable
	 */
	void postRender(HttpServletRequest request, HttpServletResponse response)
			throws Throwable;

}
