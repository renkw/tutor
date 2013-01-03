/*
 * File   AuthFilter.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.model.UserRole;

/**
 * <p>
 * 验证用户登录信息。
 * </p>
 * 
 * <p>
 * 设定参数：
 * <ul>
 * <li><strong>userRoles</strong> - 必须参数。允许的用户角色，用逗号分隔。</li>
 * <li><strong>loginPagePath</strong> - 必须参数。登录页面路径。</li>
 * <li><strong>excludePaths</strong> - 可选参数。非验证页面路径。</li>
 * </ul>
 * </p>
 * 
 * @author ren
 * 
 */
public class AuthFilter implements Filter {

	static final String USER_ROLES = "userRoles";
	static final String LOGIN_PAGE_PATH = "loginPagePath";
	static final String EXCLUDE_PATHS = "excludePaths";

	private static final Logger logger = Logger.getLogger(AuthFilter.class);

	List<UserRole> userRoles;
	String loginPagePath;
	List<String> excludePaths;

	@Override
	public void init(FilterConfig config) throws ServletException {
		if (logger.isTraceEnabled())
			logger.trace("[init] called");
		ServletContext context = config.getServletContext();

		String userRoles = config.getInitParameter(USER_ROLES);
		String loginPagePath = config.getInitParameter(LOGIN_PAGE_PATH);
		String excludePaths = config.getInitParameter(EXCLUDE_PATHS);
		if (StringUtils.isEmpty(userRoles)
				|| StringUtils.isEmpty(loginPagePath))
			throw new ServletException(
					"parameter userRoles and loginPagePath is required");

		this.userRoles = new ArrayList<UserRole>();
		for (String s : StringUtils.split(userRoles, ',')) {
			if (!(s = s.trim()).isEmpty())
				this.userRoles.add(UserRole.valueOf(s));
		}

		this.loginPagePath = context.getContextPath() + loginPagePath;

		this.excludePaths = Collections.emptyList();
		if (StringUtils.isNotEmpty(excludePaths)) {
			this.excludePaths = new ArrayList<String>();
			for (String s : StringUtils.split(excludePaths, ',')) {
				if (!(s = s.trim()).isEmpty())
					this.excludePaths.add(context.getContextPath() + s);
			}
		}
	}

	@Override
	public void destroy() {
		if (logger.isTraceEnabled())
			logger.trace("[destroy] called");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		if (logger.isTraceEnabled())
			logger.trace("[doFilter] called");
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		// check if logged in
		String uri = request.getRequestURI();
		if (logger.isDebugEnabled())
			logger.debug("[doFilter] check uri " + uri);
		if (excludePaths.contains(uri)) {
			if (logger.isDebugEnabled())
				logger.debug("[doFilter] skip login");
		} else {
			SessionContainer container = SessionContainer.get(request, false);
			if (container == null || container.getLoginUser() == null) {
				if (logger.isDebugEnabled())
					logger.debug("[doFilter] user not logged in. goto "
							+ loginPagePath);
				// TODO save request info
				response.sendRedirect(loginPagePath);
				return;
			}
			if (!userRoles.contains(container.getLoginUser().getRole())) {
				if (logger.isDebugEnabled())
					logger.debug("[doFilter] user not in role. send code 403.");
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
		}
		chain.doFilter(req, resp);
	}

}
