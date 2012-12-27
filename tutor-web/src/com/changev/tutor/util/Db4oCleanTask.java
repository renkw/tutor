/*
 * File   Db4oCleanTask.java
 * Create 2012/12/27
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.util;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;

/**
 * <p>
 * 清理db4o线程会话。
 * </p>
 * 
 * @author ren
 * 
 */
public class Db4oCleanTask extends TimerTask {

	private static final Logger logger = Logger.getLogger(Db4oCleanTask.class);

	@Override
	public void run() {
		logger.info("[run] clean invalid db4o sessions");
		Tutor.cleanLocalContainers();
	}

}
