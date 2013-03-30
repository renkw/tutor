/*
 * File   ServiceServlet.java
 * Create 2012/12/25
 * Copyright (c) change-v.com 2012
 */
package com.changev.tutor.web;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

import com.changev.tutor.Tutor;
import com.changev.tutor.web.template.RequestTemplateModel;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * <p>
 * 输出网页视图。
 * </p>
 * 
 * <p>
 * 处理过程：
 * <ol>
 * <li>根据请求模板名称，转换为spring定义的对象名称。<br>
 * 例如模板名称foo/template.html，转换后对象名称为foo.templateView。</li>
 * <li>在BeanFactory中查找对于名称的View实例，如果存在执行前处理。</li>
 * <li>输出模板内容。参考{@link freemarker.ext.servlet.FreemarkerServlet}。</li>
 * <li>如果2.中存在View实例，执行后处理。</li>
 * </ol>
 * </p>
 * 
 * @author ren
 * @see RequestTemplateModel
 */
public class ViewServlet extends HttpServlet {

	private static final long serialVersionUID = 7406529024541977198L;

	static final String KEY_TEMPLATE_MODEL = "com.changev.tutor.web.ViewServlet#KEY_TEMPLATE_MODEL";

	private static final Logger logger = Logger.getLogger(ViewServlet.class);

	Map<String, String> viewNameMap = Collections
			.synchronizedMap(new HashMap<String, String>());
	Configuration config;

	@Override
	public void init() throws ServletException {
		config = Tutor.getBeanFactory().getBean(Configuration.class);
		config.setServletContextForTemplateLoading(getServletContext(), "");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doRender(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doRender(req, resp);
	}

	/**
	 * <p>
	 * 执行View中定义的方法，然后输出模板内容。
	 * </p>
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doRender(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (logger.isTraceEnabled())
			logger.trace("[doRender] called");
		if (!preRender(req, resp))
			return;
		try {
			renderTemplate(req, resp);
		} finally {
			postRender(req, resp);
		}
	}

	/**
	 * <p>
	 * 前处理。
	 * </p>
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	protected boolean preRender(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");

		String name = getViewName(req.getServletPath());
		BeanFactory beanFactory = Tutor.getBeanFactory();
		if (beanFactory.containsBean(name)) {
			View view = (View) beanFactory.getBean(name);
			if (view != null) {
				try {
					return view.preRender(req, resp);
				} catch (RuntimeException e) {
					throw e;
				} catch (ServletException e) {
					throw e;
				} catch (IOException e) {
					throw e;
				} catch (Error e) {
					throw e;
				} catch (Throwable e) {
					throw new ServletException(e);
				}
			}
		}
		return true;
	}

	/**
	 * <p>
	 * 输出模板内容。
	 * </p>
	 * 
	 * <p>
	 * 设置HTTP响应头<br>
	 * Content-Type: text/html; charset=<i>模板编码</i><br>
	 * Progam: no-cache<br>
	 * Cache-Control: no-cache
	 * </p>
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void renderTemplate(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		if (logger.isTraceEnabled())
			logger.trace("[renderTemplate] called");
		// render template
		try {
			TemplateModel model = getTemplateModel(req);
			
			Template template = config.getTemplate(getRewritePath(req.getServletPath()));
			resp.setContentType("text/html; charset=" + template.getEncoding());
			resp.setHeader("Pragma", "no-cache");
			resp.setHeader("Cache-Control", "no-cache");
			template.process(model, resp.getWriter());
		} catch (TemplateException e) {
			throw new ServletException(e);
		}
	}

	/**
	 * <p>
	 * 后处理。
	 * </p>
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void postRender(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (logger.isTraceEnabled())
			logger.trace("[postRender] called");

		String name = getViewName(req.getServletPath());
		BeanFactory beanFactory = Tutor.getBeanFactory();
		if (beanFactory.containsBean(name)) {
			View view = (View) beanFactory.getBean(name);
			if (view != null) {
				try {
					view.postRender(req, resp);
				} catch (RuntimeException e) {
					throw e;
				} catch (ServletException e) {
					throw e;
				} catch (IOException e) {
					throw e;
				} catch (Error e) {
					throw e;
				} catch (Throwable e) {
					throw new ServletException(e);
				}
			}
		}
	}

	protected String getRewritePath(String path){
		if(path.contains("_")){
			String[] r_name = path.split("\\_");
			path = r_name[0] + ".html";
		}
		return path;
	}
	
	
	/**
	 * <p>
	 * 取得请求路径对应的View实例名称。
	 * </p>
	 * 
	 * @param path
	 * @return
	 */
	protected String getViewName(String path) {
		String name = viewNameMap.get(path);
		if (name == null) {
			int start = path.charAt(0) == '/' ? 1 : 0;
			int end = path.lastIndexOf('.');
			if (end == -1)
				end = path.length();
			name = path.substring(start, end);
			//rewrite
			if(name.contains("_")){
				String[] r_name = name.split("\\_");
				name = r_name[0];
			}
			name = StringUtils.replaceChars(name, '/',
					'.') + "View";
			viewNameMap.put(path, name);
		}
				
		return name;
	}

	/**
	 * <p>
	 * 取得模板变量模型。
	 * </p>
	 * 
	 * <p>
	 * 同一请求对象只生成一次模型对象。
	 * </p>
	 * 
	 * @param request
	 * @return
	 * @throws TemplateModelException
	 */
	protected TemplateModel getTemplateModel(HttpServletRequest request)
			throws TemplateModelException {
		TemplateModel model = (TemplateModel) request
				.getAttribute(KEY_TEMPLATE_MODEL);
		if (model == null) {
			model = new RequestTemplateModel(request, config.getObjectWrapper());
			request.setAttribute(KEY_TEMPLATE_MODEL, model);
		}
		return model;
	}

}
