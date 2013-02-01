/*
 * File   RequestTemplateModel.java
 * Create 2013/01/31
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.template;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.changev.tutor.model.UserModel;
import com.changev.tutor.model.UserRole;
import com.changev.tutor.web.SessionContainer;

import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * <p>
 * 请求模板模型。
 * </p>
 * 
 * <p>
 * 全局变量：
 * <ul>
 * <li><strong>request</strong> - {@link HttpServletRequest}实例。</li>
 * <li><strong>session</strong> - {@link HttpSession}实例。</li>
 * <li><strong>context</strong> - {@link ServletContext}实例。</li>
 * <li><strong>params</strong> - {@link HttpServletRequest#getParameterMap()
 * 请求参数}。</li>
 * <li><strong>user</strong> -
 * {@link SessionContainer#getLoginUser(HttpServletRequest) 登录用户}。</li>
 * <li><strong>userRole</strong> - {@link UserModel#getRole() 登录用户角色}。</li>
 * </ul>
 * 其他请求、会话、应用上下文所有设置属性。
 * </p>
 * 
 * @author ren
 * 
 */
public class RequestTemplateModel extends AdapterModel<HttpServletRequest>
		implements TemplateHashModel {

	static final Map<String, TemplateModel> staticModels = new HashMap<String, TemplateModel>();

	static {
		try {
			InputStream stream = RequestTemplateModel.class
					.getResourceAsStream("staticmodels.properties");
			if (stream != null) {
				Properties props = new Properties();
				props.load(stream);
				for (Enumeration<?> en = props.keys(); en.hasMoreElements();) {
					String key = (String) en.nextElement();
					String value = props.getProperty(key);
					staticModels.put(key, (TemplateModel) Class.forName(value)
							.newInstance());
				}
			}
		} catch (IOException e) {
			throw new ExceptionInInitializerError(e);
		} catch (InstantiationException e) {
			throw new ExceptionInInitializerError(e);
		} catch (IllegalAccessException e) {
			throw new ExceptionInInitializerError(e);
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	private ObjectWrapper wrapper;
	private TemplateModel requestModel;
	private TemplateModel sessionModel;
	private TemplateModel contextModel;
	private TemplateModel userModel;
	private TemplateModel userRoleModel;
	private TemplateModel paramsModel;

	public RequestTemplateModel(HttpServletRequest request,
			ObjectWrapper wrapper) {
		super(request);
		this.wrapper = wrapper;
	}

	@Override
	public TemplateModel get(String key) throws TemplateModelException {
		TemplateModel model = staticModels.get(key);
		if (model == null) {
			HttpSession session = value.getSession(false);
			// global variables
			if ("user".equals(key)) {
				if (userModel == null)
					userModel = wrapper.wrap(SessionContainer
							.getLoginUser(value));
				model = userModel;
			} else if ("params".equals(key)) {
				if (paramsModel == null)
					paramsModel = new ParameterTemplateModel(
							value.getParameterMap());
				model = paramsModel;
			} else if ("userRole".equals(key)) {
				if (userRoleModel == null) {
					UserModel user = SessionContainer.getLoginUser(value);
					userRoleModel = wrapper.wrap(user == null ? UserRole.None
							: user.getRole());
				}
				model = userRoleModel;
			} else if ("request".equals(key)) {
				if (requestModel == null)
					requestModel = wrapper.wrap(value);
				model = requestModel;
			} else if ("session".equals(key)) {
				if (sessionModel == null)
					sessionModel = session == null ? NOTHING : wrapper
							.wrap(session);
				model = sessionModel;
			} else if ("context".equals(key)) {
				if (contextModel == null)
					contextModel = wrapper.wrap(value.getServletContext());
				model = contextModel;
			} else {
				// find at any scope
				Object v = value.getAttribute(key);
				if (v == null && session != null)
					v = session.getAttribute(key);
				if (v == null)
					v = value.getServletContext().getAttribute(key);
				model = wrapper.wrap(v);
			}
		}
		return model;
	}

	@Override
	public boolean isEmpty() throws TemplateModelException {
		return false;
	}

}
