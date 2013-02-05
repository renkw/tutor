/*
 * File   FileUploadServlet.java
 * Create 2013/01/05
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.UserModel;
import com.changev.tutor.web.util.UserFileManager;

/**
 * <p>
 * 上传用户文件。
 * </p>
 * 
 * @author ren
 * 
 */
public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 3293207574680820622L;

	private static final Logger logger = Logger
			.getLogger(FileUploadServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (logger.isTraceEnabled())
			logger.trace("[doPost] called");
		upload(req, resp);
	}

	protected void upload(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (logger.isTraceEnabled())
			logger.trace("[upload] called");

		resp.setContentType("text/plain;charset=UTF-8");
		UserModel user = SessionContainer.getLoginUser(req);
		UserFileManager fm = Tutor.getBeanFactory().getBean(
				UserFileManager.class);
		try {
			List<String> list = new ArrayList<String>();
			for (Part part : req.getParts()) {
				if (part.getSize() == 0 || !"file".equals(part.getName()))
					continue;
				String header = part.getHeader("content-disposition");
				if (logger.isDebugEnabled())
					logger.debug("[upload] " + header);
				int i1 = header.indexOf("filename=\"") + 10;
				int i2 = header.indexOf('"', i1);
				String filename = header.substring(i1, i2);
				i1 = filename.lastIndexOf('.');
				String name = fm.create(user.getEmail(), i1 == -1 ? ""
						: filename.substring(i1));
				OutputStream stream = fm.write(user.getEmail(), name);
				try {
					IOUtils.copy(part.getInputStream(), stream);
					list.add(name);
				} finally {
					stream.close();
					part.delete();
				}
			}
			if (logger.isDebugEnabled())
				logger.debug("[upload] success: " + list);
			resp.getWriter().write(StringUtils.join(list, ','));
		} catch (IllegalStateException e) {
			if (logger.isDebugEnabled())
				logger.debug("[upload] file size exceed");
			resp.getWriter().write("Error:文件大小超过限制。");
		}
	}

}
