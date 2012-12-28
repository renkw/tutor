/*
 * File   ContextListener.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.changev.tutor.Tutor;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.cs.Db4oClientServer;

/**
 * <p>
 * 网站启动时加载必要的设置。
 * </p>
 * 
 * <p>
 * 上下文设定参数：
 * <ul>
 * <li><strong>log4jConfigPath</strong> - 可选参数。Log4j配置文件路径。<br />
 * 默认为{@link Tutor#DEFAULT_LOG4J_CONFIG_PATH DEFAULT_LOG4J_CONFIG_PATH}。</li>
 * <li><strong>beanConfigPath</strong> - 可选参数。Spring配置文件路径。<br />
 * 默认为{@link Tutor#DEFAULT_BEAN_CONFIG_PATH DEFAULT_BEAN_CONFIG_PATH}。</li>
 * <li><strong>db4oConnectPath</strong> - 可选参数。Db4o数据文件路径或服务器连接字符串。<br />
 * 服务器连接字符串格式：db4o:<i>host</i>:<i>port</i>?user=<i>user</i>&amp;password=<i>password</i><br />
 * 默认为{@link Tutor#DEFAULT_DATAFILE_PATH DEFAULT_DATAFILE_PATH}。</li>
 * </ul>
 * </p>
 * 
 * @author ren
 * 
 */
public class ContextListener implements ServletContextListener {

	static final String LOG4J_CONFIG_PATH = "log4jConfigPath";
	static final String BEAN_CONFIG_PATH = "beanConfigPath";
	static final String DB4O_CONNECT_PATH = "db4oConnectPath";

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();

		// set context root path
		Tutor.setContextRootPath(context.getRealPath("/"));

		// init log4j
		context.log("init log4j...");
		String log4jConfigPath = StringUtils.defaultIfEmpty(
				context.getInitParameter(LOG4J_CONFIG_PATH),
				Tutor.DEFAULT_LOG4J_CONFIG_PATH);
		context.log("log4jConfigPath = " + log4jConfigPath);
		DOMConfigurator.configure(Tutor.getRealPath(log4jConfigPath));

		// init spring beans
		context.log("init spring beans...");
		String beanConfigPath = StringUtils.defaultIfEmpty(
				context.getInitParameter(BEAN_CONFIG_PATH),
				Tutor.DEFAULT_BEAN_CONFIG_PATH);
		context.log("beanConfigPath = " + beanConfigPath);
		BeanFactory beanFactory = new FileSystemXmlApplicationContext(
				Tutor.getRealPath(beanConfigPath));
		Tutor.setBeanFactory(beanFactory);

		// init db4o
		context.log("init db4o...");
		String db4oDataPath = StringUtils.defaultIfEmpty(
				context.getInitParameter(DB4O_CONNECT_PATH),
				Tutor.DEFAULT_DATAFILE_PATH);
		context.log("db4oDataPath = " + db4oDataPath);
		ObjectContainer objc;
		if (db4oDataPath.startsWith("db4o:")) {
			// open in client-server mode
			// db4o:host:port?user=user&password=password
			String[] sa = StringUtils.split(db4oDataPath, ":?=&");
			String host = sa[1];
			int port = Integer.parseInt(sa[2]);
			String user = "", password = "";
			for (int i = 4; i < sa.length; i += 2) {
				switch (sa[i - 1]) {
				case "user":
					user = sa[i];
					break;
				case "password":
					password = sa[i];
					break;
				}
			}
			objc = Db4oClientServer.openClient(host, port, user, password);
		} else {
			// open in local file mode
			objc = Db4oEmbedded.openFile(Tutor.getRealPath(db4oDataPath));
		}
		Tutor.setTopContainer(objc.ext());

		// run startup action
		context.log("run startup...");
		if (beanFactory.containsBean("startup"))
			((Runnable) beanFactory.getBean("startup")).run();
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		ServletContext context = event.getServletContext();

		// run shutdown action
		context.log("run shutdown...");
		BeanFactory beanFactory = Tutor.getBeanFactory();
		if (beanFactory != null && beanFactory.containsBean("shutdown"))
			((Runnable) beanFactory.getBean("shutdown")).run();

		// close db4o
		context.log("close db4o...");
		ObjectContainer objc = Tutor.getTopContainer();
		if (objc != null) {
			while (!objc.close())
				;
		}
	}

}
