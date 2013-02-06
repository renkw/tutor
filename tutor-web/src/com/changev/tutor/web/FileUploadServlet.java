/*
 * File   FileUploadServlet.java
 * Create 2013/01/05
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadBase.FileUploadIOException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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

		String s = null;
		if (ServletFileUpload.isMultipartContent(req)) {
			s = uploadParts(req);
		} else {
			s = uploadStream(req);
		}

		resp.setContentType("text/plain;charset=UTF-8");
		if (s != null)
			resp.getWriter().write(s);
	}

	protected String uploadParts(HttpServletRequest req)
			throws ServletException, IOException {
		if (logger.isTraceEnabled())
			logger.trace("[uploadParts] called");

		UserModel user = SessionContainer.getLoginUser(req);
		UserFileManager fm = Tutor.getBeanFactory().getBean(
				UserFileManager.class);
		ServletFileUpload upload = Tutor.getBeanFactory().getBean(
				ServletFileUpload.class);
		try {
			List<String> list = new ArrayList<String>();
			List<?> fileItems = upload.parseRequest(req);
			for (int i = 0; i < fileItems.size(); i++) {
				FileItem fileItem = (FileItem) fileItems.get(i);
				if (fileItem.isFormField())
					continue;
				String name = fm.create(user.getEmail(),
						getSuffix(fileItem.getName()));
				InputStream in = fileItem.getInputStream();
				OutputStream out = fm.write(user.getEmail(), name);
				try {
					IOUtils.copy(in, out);
					list.add(name);
				} finally {
					in.close();
					out.close();
					fileItem.delete();
				}
			}
			String s = StringUtils.join(list, ',');
			if (logger.isDebugEnabled())
				logger.debug("[uploadParts] success: " + s);
			return s;
		} catch (FileUploadException e) {
			Throwable t = e.getCause();
			if (t instanceof FileSizeLimitExceededException) {
				if (logger.isDebugEnabled())
					logger.debug("[uploadParts] file size exceed", e);
				return "Error:文件大小超过限制。";
			} else if (t instanceof SizeLimitExceededException) {
				if (logger.isDebugEnabled())
					logger.debug("[uploadParts] file size exceed", e);
				return "Error:文件大小超过限制。";
			}
			throw new ServletException(e);
		}
	}

	protected String uploadStream(HttpServletRequest req)
			throws ServletException, IOException {
		if (logger.isTraceEnabled())
			logger.trace("[uploadStream] called");

		UserModel user = SessionContainer.getLoginUser(req);
		UserFileManager fm = Tutor.getBeanFactory().getBean(
				UserFileManager.class);

		int length = req.getContentLength();
		long maxLength = getSizeMax();
		if (maxLength != -1 && length > maxLength)
			return "Error:文件大小超过限制";

		File tmp = writeTempFile(req.getInputStream(), maxLength);
		if (tmp == null)
			return "Error:文件大小超过限制";

		InputStream in = new FileInputStream(tmp);
		String pName = req.getParameter("name");
		String name = fm.create(user.getEmail(), getSuffix(pName));
		OutputStream out = fm.write(user.getEmail(), name);
		try {
			IOUtils.copy(in, out);
			if (logger.isDebugEnabled())
				logger.debug("[uploadStream] success: " + name);
			return name;
		} catch (FileUploadIOException e) {
			if (logger.isDebugEnabled())
				logger.debug("[uploadStream] file size exceed", e);
			return "Error:文件大小超过限制。";
		} finally {
			in.close();
			out.close();
			tmp.delete();
		}
	}

	protected String getSuffix(String name) {
		if (StringUtils.isEmpty(name))
			return "";
		int i = name.lastIndexOf('.');
		return i == -1 ? "" : name.substring(i);
	}

	protected long getSizeMax() {
		ServletFileUpload upload = Tutor.getBeanFactory().getBean(
				ServletFileUpload.class);
		if (upload.getFileSizeMax() != -1)
			return upload.getFileSizeMax();
		if (upload.getSizeMax() != -1)
			return upload.getSizeMax();
		return Long.MAX_VALUE;
	}

	protected File writeTempFile(InputStream in, long maxLength)
			throws IOException {
		File tmp = File.createTempFile("upload_", null);
		OutputStream out = new FileOutputStream(tmp);
		long len = 0;
		try {
			byte[] buf = new byte[1024];
			for (int n = in.read(buf); n != -1; n = in.read(buf)) {
				len += n;
				if (len > maxLength)
					break;
				out.write(buf, 0, n);
			}
		} finally {
			out.close();
			if (len > maxLength) {
				tmp.delete();
				tmp = null;
			}
		}
		return tmp;
	}

}
