/*
 * File   ServiceServlet.java
 * Create 2012/12/25
 * Copyright (c) change-v.com 2012
 */
package com.changev.tutor.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

import com.changev.tutor.Tutor;

import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.template.Template;
import freemarker.template.TemplateModel;

/**
 * @author ren
 * 
 */
public class ViewServlet extends FreemarkerServlet {

	private static final long serialVersionUID = 7406529024541977198L;

	private static final Logger logger = Logger.getLogger(ViewServlet.class);

	@Override
	protected boolean preTemplateProcess(HttpServletRequest request,
			HttpServletResponse response, Template template, TemplateModel data)
			throws ServletException, IOException {
		if (logger.isTraceEnabled())
			logger.trace("[preTemplateProcess] called");
		// set login user to request
		SessionContainer container = SessionContainer.getInstance(request);
		if (container != null && container.getLoginUser() != null)
			request.setAttribute("loginUser", container.getLoginUser());
		// get view instance and call its preRender method if exists
		BeanFactory beanFactory = (BeanFactory) getServletContext()
				.getAttribute(Tutor.KEY_BEAN_FACTORY);
		if (beanFactory != null) {
			String beanName = getViewName(template.getName());
			if (logger.isDebugEnabled())
				logger.debug("[preTemplateProcess] beanName = " + beanName);
			if (StringUtils.isNotEmpty(beanName)
					&& beanFactory.containsBean(beanName)) {
				View view = (View) beanFactory.getBean(beanName);
				if (logger.isDebugEnabled())
					logger.debug("[preTemplateProcess] view = " + view);
				request.setAttribute(Tutor.KEY_REQUEST_VIEW, view);
				return view.preRender(request, response);
			}
			if (logger.isDebugEnabled())
				logger.debug("[preTemplateProcess] view = null");
		}
		return true;
	}

	@Override
	protected void postTemplateProcess(HttpServletRequest request,
			HttpServletResponse response, Template template, TemplateModel data)
			throws ServletException, IOException {
		if (logger.isTraceEnabled())
			logger.trace("[postTemplateProcess] called");
		// get view instance and call its postRender method if exits
		View view = (View) request.getAttribute(Tutor.KEY_REQUEST_VIEW);
		if (view != null) {
			if (logger.isDebugEnabled())
				logger.debug("[postTemplateProcess] view = " + view);
			view.postRender(request, response);
			request.removeAttribute(Tutor.KEY_REQUEST_VIEW);
		}
		// remove login user from request
		request.removeAttribute("loginUser");
	}

	/**
	 * <p>
	 * 取得模板对应的View实例名称。
	 * </p>
	 * 
	 * <p>
	 * 假设模板名为foo/template.html，对应的View实例名称应为foo.template。
	 * </p>
	 * 
	 * @param templateName
	 * @return
	 */
	protected String getViewName(String templateName) {
		int end = templateName.lastIndexOf('.');
		String beanName = end == -1 ? templateName : templateName.substring(0,
				end);
		beanName = StringUtils.replaceChars(beanName, '/', '.');
		return beanName;
	}

}
