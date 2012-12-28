/*
 * File   ServiceServlet.java
 * Create 2012/12/25
 * Copyright (c) change-v.com 2012
 */
package com.changev.tutor.web;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

import com.changev.tutor.Tutor;

import freemarker.cache.FileTemplateLoader;
import freemarker.ext.servlet.AllHttpScopesHashModel;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.HttpRequestParametersHashModel;
import freemarker.ext.servlet.HttpSessionHashModel;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * <p>
 * 输出网页视图。
 * </p>
 * 
 * <p>
 * 处理过程：
 * <ol>
 * <li>根据请求模板名称，转换为spring定义的对象名称。<br />
 * 例如模板名称foo/template.html，转换后对象名称为foo.templateView。</li>
 * <li>在BeanFactory中查找对于名称的View实例，如果存在执行前处理。</li>
 * <li>输出模板内容。参考{@link freemarker.ext.servlet.FreemarkerServlet}。</li>
 * <li>如果2.中存在View实例，执行后处理。</li>
 * </ol>
 * 
 * 模板可用公共变量：
 * <ul>
 * <li><string>{@link SessionContainer#getLoginUser() loginUser}</strong> -
 * 登录用户。</li>
 * <li><string>{@link Messages#get(HttpServletRequest) msg}</strong> - 处理消息。</li>
 * </ul>
 * </p>
 * 
 * @author ren
 * 
 */
public class ViewServlet extends HttpServlet {

	private static final long serialVersionUID = 7406529024541977198L;

	private static final Logger logger = Logger.getLogger(ViewServlet.class);

	Map<String, String> viewNameMap = new ConcurrentHashMap<>();
	Configuration config;

	@Override
	public void init() throws ServletException {
		config = (Configuration) Tutor.getBeanFactory().getBean(
				"freemarkerConfig");
		try {
			config.setTemplateLoader(new FileTemplateLoader(new File(Tutor
					.getContextRootPath())));
		} catch (IOException e) {
			throw new ServletException(e);
		}
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
				} catch (RuntimeException | ServletException | IOException
						| Error e) {
					throw e;
				} catch (Throwable e) {
					throw new ServletException(e);
				}
			}
		}
		return true;
	}

	protected void renderTemplate(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		if (logger.isTraceEnabled())
			logger.trace("[renderTemplate] called");

		HttpSession session = req.getSession(false);
		AllHttpScopesHashModel model = new AllHttpScopesHashModel(
				config.getObjectWrapper(), getServletContext(), req);
		model.put("Tutor", Tutor.SINGLETON);
		model.put("Request",
				new HttpRequestHashModel(req, config.getObjectWrapper()));
		model.put("Session",
				new HttpSessionHashModel(session, config.getObjectWrapper()));
		model.put("Application",
				new ServletContextHashModel(this, config.getObjectWrapper()));
		model.put("tutor", SessionContainer.get(req));
		model.put("request", req);
		model.put("session", session);
		model.put("application", getServletContext());
		model.put("params", new HttpRequestParametersHashModel(req));
		model.put("msg", Messages.get(req));

		Template template = config.getTemplate(req.getServletPath());
		try {
			resp.setContentType("text/html; charset=" + template.getEncoding());
			template.process(model, resp.getWriter());
		} catch (TemplateException e) {
			throw new ServletException(e);
		}
	}

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
				} catch (RuntimeException | ServletException | IOException
						| Error e) {
					throw e;
				} catch (Throwable e) {
					throw new ServletException(e);
				}
			}
		}
	}

	protected String getViewName(String path) {
		String name = viewNameMap.get(path);
		if (name == null) {
			int start = path.charAt(0) == '/' ? 1 : 0;
			int end = path.lastIndexOf('.');
			if (end == -1)
				end = path.length();
			name = StringUtils.replaceChars(path.substring(start, end), '/',
					'.') + "View";
			viewNameMap.put(path, name);
		}
		return name;
	}

	//
	// @Override
	// protected boolean preTemplateProcess(HttpServletRequest request,
	// HttpServletResponse response, Template template, TemplateModel data)
	// throws ServletException, IOException {
	// if (logger.isTraceEnabled())
	// logger.trace("[preTemplateProcess] called");
	// // set login user to request
	// SessionContainer container = SessionContainer.get(request);
	// if (container != null)
	// request.setAttribute("loginUser", container.getLoginUser());
	// // get view instance and call its preRender method if exists
	// BeanFactory beanFactory = Tutor.getBeanFactory();
	// if (beanFactory != null) {
	// String beanName = getViewName(template.getName());
	// if (logger.isDebugEnabled())
	// logger.debug("[preTemplateProcess] beanName = " + beanName);
	// if (StringUtils.isNotEmpty(beanName)
	// && beanFactory.containsBean(beanName)) {
	// View view = (View) beanFactory.getBean(beanName);
	// if (logger.isDebugEnabled())
	// logger.debug("[preTemplateProcess] view = " + view);
	// try {
	// return view.preRender(request, response);
	// } catch (RuntimeException | ServletException | IOException
	// | Error e) {
	// throw e;
	// } catch (Throwable t) {
	// throw new ServletException(t);
	// }
	// }
	// if (logger.isDebugEnabled())
	// logger.debug("[preTemplateProcess] view = null");
	// }
	// return true;
	// }
	//
	// @Override
	// protected void postTemplateProcess(HttpServletRequest request,
	// HttpServletResponse response, Template template, TemplateModel data)
	// throws ServletException, IOException {
	// if (logger.isTraceEnabled())
	// logger.trace("[postTemplateProcess] called");
	// // get view instance and call its postRender method if exits
	// BeanFactory beanFactory = Tutor.getBeanFactory();
	// if (beanFactory != null) {
	// String beanName = getViewName(template.getName());
	// if (StringUtils.isNotEmpty(beanName)
	// && beanFactory.containsBean(beanName)) {
	// View view = (View) beanFactory.getBean(beanName);
	// try {
	// view.postRender(request, response);
	// } catch (RuntimeException | ServletException | IOException
	// | Error e) {
	// throw e;
	// } catch (Throwable t) {
	// throw new ServletException(t);
	// }
	// }
	// }
	// }
	//
	// /**
	// * <p>
	// * 取得模板对应的View实例名称。
	// * </p>
	// *
	// * <p>
	// * 假设模板名为foo/template.html，对应的View实例名称应为foo.templateView。
	// * </p>
	// *
	// * @param templateName
	// * @return
	// */
	// protected String getViewName(String templateName) {
	// int end = templateName.lastIndexOf('.');
	// String name = end == -1 ? templateName : templateName.substring(0, end);
	// name = StringUtils.replaceChars(name, '/', '.');
	// return name + "View";
	// }

}
