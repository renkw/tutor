/*
 * File   ConstantsReloadTask.java
 * Create 2013/01/19
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.TimerTask;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
import com.google.gson.Gson;

/**
 * <p>
 * 自动加载系统常量。
 * </p>
 * 
 * @author ren
 * 
 */
public class ConstantsReloadTask extends TimerTask {

	private static final Logger logger = Logger
			.getLogger(ConstantsReloadTask.class);

	private File file;
	private long lastModified;

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		if (file != null) {
			long lm = file.lastModified();
			if (lm != lastModified) {
				lastModified = lm;
				if (logger.isDebugEnabled())
					logger.debug("[run] reload " + file.getPath());
				Reader reader = null;
				try {
					reader = new InputStreamReader(new FileInputStream(file),
							Tutor.DEFAULT_ENCODING);
					Map<String, Object> constants = (Map<String, Object>) Tutor
							.getBeanFactory().getBean(Gson.class)
							.fromJson(reader, Object.class);
					Tutor.setConstants(constants);
				} catch (IOException e) {
					logger.error("[run] read constants file error", e);
				} finally {
					IOUtils.closeQuietly(reader);
				}
			}
		}
	}

	/**
	 * @return the filepath
	 */
	public String getFilepath() {
		return file == null ? null : file.getPath();
	}

	/**
	 * @param filepath
	 *            the filepath to set
	 */
	public void setFilepath(String filepath) {
		file = StringUtils.isEmpty(filepath) ? null : new File(
				Tutor.getRealPath(filepath));
	}

}
