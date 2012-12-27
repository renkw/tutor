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
 * 设定参数：
 * <ul>
 * <li><strong>maxFileSize</strong> - 可选参数。最大允许的上传文件大小（bytes）。<br />
 * 默认表示无限制。</li>
 * <li><strong>acceptContentTypes</strong> - 可选参数。允许的上传文件类型。<br />
 * 可指定泛类型，如image/*等。默认表示无限制。</li>
 * <li><strong>uploadDir</strong> - 可选参数。上传文件目录。<br />
 * 默认为{@link com.changev.tutor.Tutor#DEFAULT_UPLOAD_PATH}。
 * </ul>
 * 符合maxFileSize和acceptContentTypes的上传文件将被保存在uploadDir中。 返回客户端文件序列ID。
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
