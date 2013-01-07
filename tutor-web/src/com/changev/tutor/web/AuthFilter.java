/*
 * File   AuthFilter.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.UserRole;

/**
 * <p>
 * 验证用户登录信息。
 * </p>
 * 
 * <p>
 * 设定参数：
 * <ul>
 * <li><strong>ACL</strong> - 可选参数。访问控制列表，默认为全部允许。</li>
 * </ul>
 * 
 * ACL规则格式： <strong>pattern</strong> <i>spaces</i> <strong>roles</strong>
 * <i>spaces</i> <strong>forward_path</strong><br />
 * <strong>pattern:</strong> *|<i>path_pattern</i><br />
 * &nbsp;&nbsp;*表示全部；path_pattern中可使用通配符(*)表示任意某一路径名或文件名<br />
 * <strong>roles:</strong> (+|-)<i>role_name</i><br />
 * &nbsp;&nbsp;+表示允许访问；-表示拒绝访问；默认为+<br />
 * &nbsp;&nbsp;设置多个角色用|分隔<br />
 * <strong>forward_path:</strong> (F|R)<i>path</i><br />
 * &nbsp;&nbsp;F表示请求转发（forward）；R表示响应重定向（redirect）；默认为F<br />
 * <br />
 * 
 * 满足pattern的第一条规则决定可访问与否。
 * </p>
 * 
 * @author ren
 * 
 */
public class AuthFilter implements Filter {

	private static final Logger logger = Logger.getLogger(AuthFilter.class);

	/** 允许访问 */
	public static final int ACCEPTED = 0;

	/** 不允许访问 */
	public static final int NOT_ACCEPTED = 1;

	/** 拒绝访问 */
	public static final int DENIED = 2;

	static final String ACL = "ACL";
	static final String ACL_PAT = "^\\s*([^\\s]+)\\s*([^\\s]*)\\s*([FR]?)([^\\s]*)\\s*$";

	static AccessControlRule parseAcl(String str) throws ParseException {
		if (logger.isTraceEnabled())
			logger.trace("[parseAcl] called");

		AccessControlRule item = null, head = null;
		// parse
		Matcher matcher = Pattern.compile(ACL_PAT, Pattern.MULTILINE).matcher(
				str);
		int end = 0;
		while (matcher.find()) {
			end = matcher.end();
			if (item == null)
				item = head = new AccessControlRule();
			else
				item = item.next = new AccessControlRule();
			// get options
			String sPat = matcher.group(1);
			String sRoles = StringUtils.defaultString(matcher.group(2), "*");
			String sForward = StringUtils.defaultString(matcher.group(3), "F");
			String sPath = matcher.group(4);
			// set rule
			item.pattern = Pattern.compile(sPat);
			item.pattern = "*".equals(sPat) ? null : Pattern.compile(Pattern
					.quote(sPat).replace("*", "\\E[^/]*\\Q")
					.replace("\\Q\\E", ""));
			if (!"*".equals(sRoles)) {
				for (String roleName : StringUtils.split(sRoles, '|')) {
					if (roleName.startsWith("-")) {
						item.denyRoles = (UserRole[]) ArrayUtils.add(
								item.denyRoles,
								UserRole.valueOf(roleName.substring(1)));
					} else {
						if (roleName.startsWith("+"))
							roleName = roleName.substring(1);
						item.acceptRoles = (UserRole[]) ArrayUtils.add(
								item.acceptRoles, UserRole.valueOf(roleName));
					}
				}
			}
			item.forward = "F".equals(sForward);
			item.path = sPath;

			if (logger.isDebugEnabled())
				logger.debug("[parseAcl] add rule: " + item);
		}
		// check format
		while (end < str.length()) {
			if (!Character.isWhitespace(str.charAt(end)))
				throw new ParseException(str, end);
			end++;
		}
		return head;
	}

	Map<String, AccessControlRule> ruleMap;
	AccessControlRule acl;

	@Override
	public void init(FilterConfig config) throws ServletException {
		if (logger.isTraceEnabled())
			logger.trace("[init] called");

		String sAcl = config.getInitParameter(ACL);
		if (StringUtils.isNotBlank(sAcl)) {
			try {
				acl = parseAcl(sAcl);
			} catch (ParseException e) {
				throw new ServletException(e);
			}
		}
		if (acl != null) {
			ruleMap = Collections
					.synchronizedMap(new HashMap<String, AccessControlRule>());
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
		// get request path
		String reqPath = request.getRequestURI().substring(
				request.getContextPath().length());
		// get user role
		UserRole role = UserRole.None;
		SessionContainer sess = SessionContainer.get(request, false);
		if (sess != null && sess.getLoginUser() != null)
			role = sess.getLoginUser().getRole();
		if (logger.isDebugEnabled()) {
			logger.debug("[doFilter] reqPath = " + reqPath);
			logger.debug("[doFilter] role = " + role);
		}
		// check with ACL
		if (acl != null) {
			AccessControlRule item;
			if (ruleMap.containsKey(reqPath)) {
				item = ruleMap.get(reqPath);
			} else {
				item = acl;
				while (item != null && !item.match(reqPath))
					item = item.next;
				ruleMap.put(reqPath, item);
			}
			int reason;
			if (item != null && (reason = item.accept(role)) != ACCEPTED) {
				if (logger.isDebugEnabled())
					logger.debug("[doFilter] failed rule: " + item);
				// goto login page
				String path = getForwardPath(item.path, reason,
						request.getRequestURI(), request.getQueryString());
				if (item.forward)
					request.getRequestDispatcher(path).forward(req, resp);
				else
					response.sendRedirect(request.getContextPath() + path);
				return;
			}
		}
		// accept
		if (logger.isDebugEnabled())
			logger.debug("[doFilter] accepted.");
		chain.doFilter(req, resp);
	}

	private String getForwardPath(String path, int reason, String uri,
			String qstr) throws UnsupportedEncodingException {
		String url = StringUtils.isEmpty(qstr) ? uri : uri + "?" + qstr;
		return path.replace("{reason}", Integer.toString(reason)).replace(
				"{url}", URLEncoder.encode(url, Tutor.DEFAULT_ENCODING));
	}

	/**
	 * <p>
	 * 访问控制规则。
	 * </p>
	 * 
	 * @author ren
	 * 
	 */
	static class AccessControlRule {

		Pattern pattern;
		UserRole[] acceptRoles;
		UserRole[] denyRoles;
		boolean forward;
		String path;
		AccessControlRule next;

		public boolean match(String path) {
			return pattern == null || pattern.matcher(path).find();
		}

		public int accept(UserRole role) {
			if (acceptRoles != null && !ArrayUtils.contains(acceptRoles, role))
				return NOT_ACCEPTED;
			if (denyRoles != null && ArrayUtils.contains(denyRoles, role))
				return DENIED;
			return ACCEPTED;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			// pattern
			if (pattern == null)
				sb.append('*');
			else
				sb.append(pattern.pattern());
			sb.append(' ');
			// roles
			if (acceptRoles == null && denyRoles == null) {
				sb.append('*').append(' ');
			} else {
				if (acceptRoles != null) {
					for (UserRole role : acceptRoles)
						sb.append('+').append(role.name()).append('|');
				}
				if (denyRoles != null) {
					for (UserRole role : denyRoles)
						sb.append('-').append(role.name()).append('|');
				}
				sb.setCharAt(sb.length() - 1, ' ');
			}
			// forward path
			if (StringUtils.isNotEmpty(path))
				sb.append(forward ? 'F' : 'R').append(' ').append(path);
			return sb.toString();
		}

	}

}
