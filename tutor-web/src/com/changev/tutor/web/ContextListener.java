/*
 * File   ContextListener.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.changev.tutor.ObjectContainerWrapper;
import com.changev.tutor.Tutor;
import com.changev.tutor.util.Db4oConfigurator;
import com.db4o.ObjectContainer;

/**
 * <p>
 * 网站启动时加载必要的设置。
 * </p>
 * 
 * <p>
 * 上下文设定参数：
 * <ul>
 * <li><strong>log4jConfigPath</strong> - 可选参数。Log4j配置文件路径。<br>
 * 默认为{@link Tutor#DEFAULT_LOG4J_CONFIG_PATH DEFAULT_LOG4J_CONFIG_PATH}。</li>
 * <li><strong>beanConfigPath</strong> - 可选参数。Spring配置文件路径。<br>
 * 默认为{@link Tutor#DEFAULT_BEAN_CONFIG_PATH DEFAULT_BEAN_CONFIG_PATH}。</li>
 * <li><strong>db4oConfigPath</strong> - 可选参数。Db4o配置文件路径。<br>
 * 默认为{@link Tutor#DEFAULT_DB4O_CONFIG_PATH DEFAULT_DB4O_CONFIG_PATH}。</li>
 * </ul>
 * </p>
 * 
 * @author ren
 * 
 */
public class ContextListener implements ServletContextListener,
		HttpSessionListener, ServletRequestListener {

	static final String LOG4J_CONFIG_PATH = "log4jConfigPath";
	static final String BEAN_CONFIG_PATH = "beanConfigPath";
	static final String DB4O_CONFIG_PATH = "db4oConfigPath";

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();

		// set context root path
		Tutor.setContextPath(context.getContextPath());
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
		BeanFactory beanFactory = new FileSystemXmlApplicationContext("file:"
				+ Tutor.getRealPath(beanConfigPath));
		Tutor.setBeanFactory(beanFactory);

		// init db4o
		context.log("init db4o...");
		String db4oConfigPath = StringUtils.defaultIfEmpty(
				context.getInitParameter(DB4O_CONFIG_PATH),
				Tutor.DEFAULT_DB4O_CONFIG_PATH);
		context.log("db4oConfigPath = " + db4oConfigPath);
		Properties props = new Properties();
		try {
			FileInputStream stream = new FileInputStream(
					Tutor.getRealPath(db4oConfigPath));
			if (db4oConfigPath.endsWith(".xml"))
				props.loadFromXML(stream);
			else
				props.load(stream);
			props.put("@root", Tutor.getContextRootPath());
			Tutor.setRootContainer(Db4oConfigurator.open(props).ext());
		} catch (IOException e) {
			context.log("read db4o config error", e);
		}

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
		ObjectContainer objc = Tutor.getRootContainer();
		if (objc != null) {
			while (!objc.close())
				;
		}
	}

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		// set current object container
		// session.setAttribute(Tutor.KEY_OBJECT_CONTAINER,
		// new ObjectContainerWrapper());
		// set session container
		session.setAttribute(Tutor.KEY_SESSION_CONTAINER,
				new SessionContainer());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		// logout
		SessionContainer container = (SessionContainer) session
				.getAttribute(Tutor.KEY_SESSION_CONTAINER);
		if (container != null) {
			session.removeAttribute(Tutor.KEY_SESSION_CONTAINER);
			container.logout();
		}
		// close current object container
		ObjectContainer objc = (ObjectContainer) session
				.getAttribute(Tutor.KEY_OBJECT_CONTAINER);
		if (objc != null) {
			session.removeAttribute(Tutor.KEY_OBJECT_CONTAINER);
			objc.close();
		}
	}

	@SuppressWarnings("unused")
	@Override
	public void requestInitialized(ServletRequestEvent event) {
		HttpServletRequest request = (HttpServletRequest) event
				.getServletRequest();
		HttpSession session = request.getSession(false);
		// set character encoding
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			event.getServletContext().log("utf-8 is unsupported", e);
		}
		// set current object container
		ObjectContainer objc = null;
		// if (session != null) {
		// objc = (ObjectContainer) session
		// .getAttribute(Tutor.KEY_OBJECT_CONTAINER);
		// }
		if (objc == null) {
			objc = new ObjectContainerWrapper(true);
			request.setAttribute(Tutor.KEY_OBJECT_CONTAINER, objc);
		}
		Tutor.setCurrentContainer(objc);
	}

	@Override
	public void requestDestroyed(ServletRequestEvent event) {
		HttpServletRequest request = (HttpServletRequest) event
				.getServletRequest();
		// close current object container
		Tutor.setCurrentContainer(null);
		ObjectContainer objc = (ObjectContainer) request
				.getAttribute(Tutor.KEY_OBJECT_CONTAINER);
		if (objc != null) {
			request.removeAttribute(Tutor.KEY_OBJECT_CONTAINER);
			objc.close();
		}
	}

}
