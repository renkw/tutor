/*
 * File   TestService.java
 * Create 2012/12/30
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.service;

import org.apache.log4j.Logger;

import com.changev.tutor.model.UserModel;
import com.changev.tutor.web.Service;
import com.db4o.ObjectContainer;

/**
 * <p>
 * 测试服务。
 * </p>
 * 
 * @author ren
 * 
 */
public class TestService implements Service<Object> {

	private static final Logger logger = Logger.getLogger(TestService.class);

	@Override
	public Object run(UserModel user, Object input, ObjectContainer objc)
			throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[run] called");
		return input;
	}

}
