/*
 * File   ServiceServlet.java
 * Create 2012/12/25
 * Copyright (c) change-v.com 2012
 */
package com.changev.tutor.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

import com.changev.tutor.Tutor;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleCollection;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import freemarker.template.TemplateSequenceModel;

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
 * 
 * 模板可用公共变量：
 * <ul>
 * <li><string>{@link Tutor Tutor}</strong> - 公共方法常量。</li>
 * <li><string>{@link SessionContainer tutor}</strong> - 会话容器。</li>
 * <li><string>{@link HttpServletRequest#getAttribute(String) Request}</strong>
 * - 请求变量域。</li>
 * <li><string>{@link HttpSession#getAttribute(String) Session}</strong> -
 * 会话变量域。</li>
 * <li><string>{@link ServletContext#getAttribute(String) Application}</strong>
 * - 上下文变量域。</li>
 * <li><string>{@link HttpServletRequest request}</strong> - 请求对象。</li>
 * <li><string>{@link HttpSession session}</strong> - 会话对象。</li>
 * <li><string>{@link ServletContext application}</strong> - 上下文对象。</li>
 * <li><string>{@link HttpServletRequest#getParameter(String) params}</strong> -
 * 请求参数。</li>
 * <li><string>{@link Messages messages}</strong> - 处理消息。</li>
 * </ul>
 * </p>
 * 
 * @author ren
 * 
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
			Template template = config.getTemplate(req.getServletPath());
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
			name = StringUtils.replaceChars(path.substring(start, end), '/',
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
			model = new RootTemplateModel(request, config.getObjectWrapper());
			request.setAttribute(KEY_TEMPLATE_MODEL, model);
		}
		return model;
	}

	// 根模板模型
	static class RootTemplateModel extends AttrTemplateModel {

		static final String[] NAMES = { "Application", // 0
				"Request", // 1
				"Session", // 2
				"Tutor", // 3
				"application", // 4
				"messages", // 5
				"params", // 6
				"request", // 7
				"session", // 8
				"tutor" // 9
		};

		TemplateModel[] preserved;

		public RootTemplateModel(HttpServletRequest request,
				ObjectWrapper wrapper) throws TemplateModelException {
			super(request, request.getSession(false), request
					.getServletContext(), wrapper);
		}

		TemplateModel create(int n) throws TemplateModelException {
			switch (n) {
			case 0:
				return new AttrTemplateModel(null, null, context, wrapper);
			case 1:
				return new AttrTemplateModel(request, null, null, wrapper);
			case 2:
				return new AttrTemplateModel(null, session, null, wrapper);
			case 3:
				return wrapper.wrap(Tutor.SINGLETON);
			case 4:
				return wrapper.wrap(context);
			case 5:
				return new MessagesHashModel(Messages.get(request, false));
			case 6:
				return new ParamHashModel(request.getParameterMap());
			case 7:
				return wrapper.wrap(request);
			case 8:
				return wrapper.wrap(session);
			case 9:
				return wrapper.wrap(SessionContainer.get(request, false));
			}
			return null;
		}

		@Override
		public TemplateModel get(String key) throws TemplateModelException {
			int i = Arrays.binarySearch(NAMES, key);
			if (i < 0)
				return super.get(key);
			if (preserved == null)
				preserved = new TemplateModel[NAMES.length];
			if (preserved[i] == null)
				preserved[i] = create(i);
			return preserved[i];
		}

		@Override
		public boolean isEmpty() throws TemplateModelException {
			return false;
		}

	}

	// 属性模板模型
	static class AttrTemplateModel implements TemplateHashModel {

		HttpServletRequest request;
		HttpSession session;
		ServletContext context;
		ObjectWrapper wrapper;

		Map<String, TemplateModel> map = Collections.emptyMap();

		AttrTemplateModel(HttpServletRequest request, HttpSession session,
				ServletContext context, ObjectWrapper wrapper) {
			this.request = request;
			this.session = session;
			this.context = context;
			this.wrapper = wrapper;
		}

		@Override
		public TemplateModel get(String key) throws TemplateModelException {
			TemplateModel model = null;
			if (map.containsKey(key)) {
				model = map.get(key);
			} else {
				Object value = null;
				if (request != null)
					value = request.getAttribute(key);
				if (value == null && session != null)
					value = session.getAttribute(key);
				if (value == null && context != null)
					value = context.getAttribute(key);
				if (value != null)
					model = wrapper.wrap(value);
				if (map == Collections.EMPTY_MAP)
					map = new HashMap<String, TemplateModel>();
				map.put(key, model);
			}
			return model;
		}

		@Override
		public boolean isEmpty() throws TemplateModelException {
			return false;
		}

	}

	// 消息模板模型
	static class MessagesHashModel implements TemplateHashModel {

		Messages messages;
		TemplateModel errorsModel;
		TemplateModel messagesModel;
		TemplateModel warningsModel;

		MessagesHashModel(Messages messages) {
			this.messages = messages;
		}

		@Override
		public TemplateModel get(String key) throws TemplateModelException {
			if ("errors".equals(key)) {
				if (errorsModel == null)
					errorsModel = messages == null ? NOTHING
							: new MessageHashModel(messages.getErrors());
				return errorsModel;
			}
			if ("messages".equals(key)) {
				if (messagesModel == null)
					messagesModel = messages == null ? NOTHING
							: new MessageHashModel(messages.getMessages());
				return messagesModel;
			}
			if ("warnings".equals(key)) {
				if (warningsModel == null)
					warningsModel = messages == null ? NOTHING
							: new MessageHashModel(messages.getWarnings());
				return warningsModel;
			}
			return null;
		}

		@Override
		public boolean isEmpty() throws TemplateModelException {
			return messages.isEmpty();
		}

	}

	// 消息模板模型
	static class MessageHashModel implements TemplateHashModelEx {

		Map<String, String> message;
		TemplateCollectionModel keys;
		TemplateCollectionModel values;

		public MessageHashModel(Map<String, String> message) {
			this.message = message;
		}

		@Override
		public TemplateModel get(String key) throws TemplateModelException {
			String value = message.get(key);
			return value == null ? null : new SimpleScalar(value);
		}

		@Override
		public boolean isEmpty() throws TemplateModelException {
			return message.isEmpty();
		}

		@Override
		public int size() throws TemplateModelException {
			return message.size();
		}

		@Override
		public TemplateCollectionModel keys() throws TemplateModelException {
			if (keys == null)
				keys = new SimpleCollection(message.keySet());
			return keys;
		}

		@Override
		public TemplateCollectionModel values() throws TemplateModelException {
			if (values == null)
				values = new SimpleCollection(message.values());
			return values;
		}

	}

	// 参数模板模型
	static class ParamHashModel implements TemplateHashModel {

		Map<String, String[]> params;

		public ParamHashModel(Map<String, String[]> params) {
			this.params = params;
		}

		@Override
		public TemplateModel get(String key) throws TemplateModelException {
			String[] values = params.get(key);
			return values == null ? null : new ParamScalarModel(values);
		}

		@Override
		public boolean isEmpty() throws TemplateModelException {
			return params.isEmpty();
		}

	}

	// 参数模板模型
	static class ParamScalarModel implements TemplateScalarModel,
			TemplateSequenceModel {

		String[] values;

		public ParamScalarModel(String[] values) {
			this.values = values;
		}

		@Override
		public String getAsString() throws TemplateModelException {
			return values[0].toString();
		}

		@Override
		public TemplateModel get(int index) throws TemplateModelException {
			return index < 0 || index >= values.length ? null
					: new SimpleScalar(values[index]);
		}

		@Override
		public int size() throws TemplateModelException {
			return values.length;
		}

	}

}
