/*
 * File   AuthFilter.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.model.UserRole;

/**
 * @author ren
 * 
 */
public class AuthFilter implements Filter {

	static final String USER_ROLES = "userRoles";
	static final String LOGIN_PAGE_PATH = "loginPagePath";
	static final String EXCLUDE_PATHS = "excludePaths";

	private static final Logger logger = Logger.getLogger(AuthFilter.class);

	Set<UserRole> userRoles;
	String loginPagePath;
	Set<String> excludePaths;

	@Override
	public void init(FilterConfig config) throws ServletException {
		if (logger.isTraceEnabled())
			logger.trace("[init] called");
		ServletContext context = config.getServletContext();

		String userRoles = config.getInitParameter(USER_ROLES);
		String loginPagePath = config.getInitParameter(LOGIN_PAGE_PATH);
		String excludePaths = config.getInitParameter(EXCLUDE_PATHS);
		if (StringUtils.isBlank(userRoles)
				|| StringUtils.isBlank(loginPagePath))
			throw new ServletException(
					"parameter userRoles and loginPagePath is required");

		this.userRoles = new HashSet<>();
		for (String s : StringUtils.split(userRoles, ','))
			this.userRoles.add(UserRole.valueOf(s.trim()));

		this.loginPagePath = context.getContextPath() + loginPagePath;

		this.excludePaths = Collections.emptySet();
		if (StringUtils.isNotBlank(excludePaths)) {
			this.excludePaths = new HashSet<>();
			for (String s : StringUtils.split(excludePaths, ','))
				this.excludePaths.add(context.getContextPath() + s.trim());
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
			SessionContainer container = SessionContainer.getInstance(request);
			if (container == null || container.getLoginUser() == null
					|| !userRoles.contains(container.getLoginUser().getRole())) {
				if (logger.isDebugEnabled())
					logger.debug("[doFilter] user not logged in. goto "
							+ loginPagePath);
				// TODO save request info
				response.sendRedirect(loginPagePath);
				return;
			}
		}
		chain.doFilter(req, resp);
	}

}
