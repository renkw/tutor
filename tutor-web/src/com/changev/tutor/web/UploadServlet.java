/*
 * File   UploadServlet.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * <p>
 * 处理上传文件。
 * </p>
 * 
 * <p>
 * 符合maxFileSize和acceptContentTypes的上传文件将被保存在uploadDir中。
 * 返回客户端文件序列ID。
 * </p>
 * 
 * @author ren
 * 
 */
public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = -6368490659256286704L;

	private static final Logger logger = Logger.getLogger(UploadServlet.class);

	String uploadDir;
	long maxFileSize;
	Set<String> acceptContentTypes;

	@Override
	public void init() throws ServletException {
		if (logger.isTraceEnabled())
			logger.trace("[init] called");
		// TODO
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (logger.isTraceEnabled())
			logger.trace("[doPost] called");
		// TODO
	}

}
