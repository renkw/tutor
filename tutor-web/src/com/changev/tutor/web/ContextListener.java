/*
 * File   ContextListener.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.changev.tutor.Tutor;

/**
 * <p>
 * 网站启动时加载必要的设置。
 * </p>
 * 
 * <p>
 * 上下文设定参数：
 * <ul>
 * <li><strong>log4jConfigPath</strong> - 可选参数。Log4j配置文件路径。<br />
 * 默认为{@link com.changev.tutor.Tutor#DEFAULT_LOG4J_CONFIG_PATH}。</li>
 * <li><strong>beanConfigPath</strong> - 可选参数。Spring配置文件路径。<br />
 * 默认为{@link com.changev.tutor.Tutor#DEFAULT_BEAN_CONFIG_PATH}。</li>
 * </ul>
 * </p>
 * 
 * @author ren
 * 
 */
public class ContextListener implements ServletContextListener {

	static final String LOG4J_CONFIG_PATH = "log4jConfigPath";
	static final String BEAN_CONFIG_PATH = "beanConfigPath";

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();

		// set context root path
		Tutor.setContextRootPath(context.getRealPath("/"));

		// init log4j
		context.log("init log4j...");
		String log4jConfigPath = StringUtils.defaultIfBlank(
				context.getInitParameter(LOG4J_CONFIG_PATH),
				Tutor.DEFAULT_LOG4J_CONFIG_PATH);
		context.log("log4jConfigPath = " + log4jConfigPath);
		DOMConfigurator.configure(Tutor.getRealPath(log4jConfigPath));

		// init spring beans
		context.log("init spring beans...");
		String beanConfigPath = StringUtils.defaultIfBlank(
				context.getInitParameter(BEAN_CONFIG_PATH),
				Tutor.DEFAULT_BEAN_CONFIG_PATH);
		context.log("beanConfigPath = " + beanConfigPath);
		BeanFactory beanFactory = new FileSystemXmlApplicationContext(
				Tutor.getRealPath(beanConfigPath));
		context.setAttribute(Tutor.KEY_BEAN_FACTORY, beanFactory);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}

}
