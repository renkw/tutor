/*
 * File   DefaultUserFileManager.java
 * Create 2013/01/02
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.changev.tutor.Tutor;

/**
 * <p>
 * 默认使用本地文件系统存储用户数据。
 * </p>
 * 
 * @author ren
 * 
 */
public class DefaultUserFileManager implements UserFileManager {

	private String workDir = Tutor.DEFAULT_UPLOAD_PATH;

	/**
	 * <p>
	 * 在本地文件系统中创建新文件。
	 * </p>
	 * 
	 * <p>
	 * 新建文件路径：<i>workDir</i>/<i>user</i><br />
	 * 新建文件名格式：yyMMddHHmm_<i>[000-999]</i>.<i>ext</i>
	 * </p>
	 * 
	 * @param user
	 * @param ext
	 * @return
	 * @throws IOException
	 */
	@Override
	public String create(String user, String ext) throws IOException {
		File dir = new File(Tutor.getRealPath(workDir), user);
		dir.mkdirs();
		// make file name
		StringBuilder nameBuf = new StringBuilder(Tutor.formatDate(new Date(),
				"yyMMddHHmm")).append('_').append("000");
		int pos = nameBuf.length() - 1;
		if (StringUtils.isNotEmpty(ext)) {
			if (ext.charAt(0) != '.')
				nameBuf.append('.');
			nameBuf.append(ext);
		}
		// create file
		String name = nameBuf.toString();
		for (int i = 0; (!new File(dir, name).createNewFile() && i < 1000); i++) {
			nameBuf.setCharAt(pos, (char) ('0' + i % 10));
			nameBuf.setCharAt(pos - 1, (char) ('0' + i / 10 % 10));
			nameBuf.setCharAt(pos - 2, (char) ('0' + i / 100 % 10));
			name = nameBuf.toString();
		}
		return name;
	}

	@Override
	public InputStream read(String user, String file) throws IOException {
		if (file.indexOf('/') != -1 || file.indexOf('\\') != -1)
			throw new IllegalArgumentException("file = " + file);
		File dir = new File(Tutor.getRealPath(workDir), user);
		return readFile(new File(dir, file));
	}

	@Override
	public OutputStream write(String user, String file) throws IOException {
		if (file.indexOf('/') != -1 || file.indexOf('\\') != -1)
			throw new IllegalArgumentException("file = " + file);
		File dir = new File(Tutor.getRealPath(workDir), user);
		return writeFile(new File(dir, file));
	}

	/**
	 * <p>
	 * 新建文件。
	 * </p>
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	protected boolean createNewFile(File file) throws IOException {
		return file.createNewFile();
	}

	/**
	 * <p>
	 * 读文件。
	 * </p>
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	protected InputStream readFile(File file) throws IOException {
		return new FileInputStream(file);
	}

	/**
	 * <p>
	 * 写文件。
	 * </p>
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	protected OutputStream writeFile(File file) throws IOException {
		return new FileOutputStream(file);
	}

	/**
	 * @return the workDir
	 */
	public String getWorkDir() {
		return workDir;
	}

	/**
	 * @param workDir
	 *            the workDir to set
	 */
	public void setWorkDir(String workDir) {
		this.workDir = workDir;
	}

}
