/*
 * File   Db4oBackupTask.java
 * Create 2012/12/27
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;

/**
 * <p>
 * 执行db4o备份。
 * </p>
 * 
 * @author ren
 * 
 */
public class Db4oBackupTask extends TimerTask {

	private static final Logger logger = Logger.getLogger(Db4oBackupTask.class);

	private String filepath;

	@Override
	public void run() {
		if (Tutor.getTopContainer() != null && StringUtils.isNotEmpty(filepath)) {
			int i = filepath.lastIndexOf('.');
			String path = (i != -1 ? filepath.substring(0, i) : filepath) + "_"
					+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			if (i != -1)
				path += filepath.substring(i);
			logger.info("[run] backup data file to " + path);
			Tutor.getTopContainer().backup(path);
		}
	}

	/**
	 * @return the filepath
	 */
	public String getFilepath() {
		return filepath;
	}

	/**
	 * @param filepath
	 *            the filepath to set
	 */
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

}
