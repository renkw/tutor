/*
 * File   FileDownloadServlet.java
 * Create 2013/01/05
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.UserModel;
import com.changev.tutor.web.util.UserFileManager;

/**
 * <p>
 * 下载用户文件。
 * </p>
 * 
 * @author ren
 * 
 */
public class FileDownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 3293207574680820622L;

	private static final Logger logger = Logger
			.getLogger(FileDownloadServlet.class);

	static final String DEFAULT_MIME_TYPE = "application/octet-stream";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (logger.isTraceEnabled())
			logger.trace("[doGet] called");
		download(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (logger.isTraceEnabled())
			logger.trace("[doPost] called");
		download(req, resp);
	}

	protected void download(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (logger.isTraceEnabled())
			logger.trace("[download] called");

		String path = StringUtils.defaultString(req.getPathInfo());
		int sep = path.lastIndexOf('/');
		if (sep != -1) {
			String user = path.substring(1, sep);
			String file = path.substring(sep + 1);
			if (logger.isDebugEnabled()) {
				logger.debug("[download] user = " + user);
				logger.debug("[download] filename = " + file);
			}

			UserModel userModel = Tutor.fromId(user);
			// TODO privacy
			InputStream input = Tutor.getBeanFactory()
					.getBean(UserFileManager.class)
					.read(userModel.getEmail(), file);
			String ctype = getServletContext().getMimeType(file);
			resp.setContentType(StringUtils.defaultString(ctype,
					DEFAULT_MIME_TYPE));
			OutputStream output = resp.getOutputStream();
			IOUtils.copy(input, output);
			output.close();
		}
	}

}
