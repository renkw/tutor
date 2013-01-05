/*
 * File   UserFileManager.java
 * Create 2013/01/02
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>
 * 用户文件管理器。
 * </p>
 * 
 * @author ren
 * 
 */
public interface UserFileManager {

	/**
	 * <p>
	 * 新建用户文件，返回文件名。
	 * </p>
	 * 
	 * @param user
	 * @param ext
	 * @return
	 * @throws IOException
	 */
	String create(String user, String ext) throws IOException;

	/**
	 * <p>
	 * 读用户文件。
	 * </p>
	 * 
	 * @param user
	 * @param file
	 * @return
	 * @throws IOException
	 */
	InputStream read(String user, String file) throws IOException;

	/**
	 * <p>
	 * 写用户文件。
	 * </p>
	 * 
	 * @param user
	 * @param file
	 * @return
	 * @throws IOException
	 */
	OutputStream write(String user, String file) throws IOException;

}
