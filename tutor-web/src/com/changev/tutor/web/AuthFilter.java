/*
 * File   AuthFilter.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.UserModel;
import com.changev.tutor.model.UserRole;
import com.changev.tutor.model.UserState;

/**
 * <p>
 * 验证用户登录信息。
 * </p>
 * 
 * <p>
 * 设定参数：
 * <ul>
 * <li><strong>ACL</strong> - 可选参数。访问控制文件路径，默认为{@link Tutor#DEFAULT_ACL_PATH
 * DEFAULT_ACL_PATH}。</li>
 * </ul>
 * 满足pattern的第一条规则决定可访问与否。
 * </p>
 * 
 * @author ren
 * 
 */
public class AuthFilter implements Filter {

	private static final Logger logger = Logger.getLogger(AuthFilter.class);

	static final String ACL = "ACL";

	static AccessControlRule parseAcl(String path) throws IOException,
			XMLStreamException {
		if (logger.isTraceEnabled())
			logger.trace("[parseAcl] called");

		AccessControlRule head = null, tail = null;
		// read xml
		InputStream stream = new FileInputStream(path);
		try {
			XMLEventReader reader = XMLInputFactory.newFactory()
					.createXMLEventReader(stream);
			XMLEvent evt = reader.nextTag();
			// acl
			if (!"acl".equals(evt.asStartElement().getName().getLocalPart()))
				throw new XMLStreamException("root tag must be acl");
			while (true) {
				evt = reader.nextTag();
				if (evt.isStartElement()) {
					// rule
					StartElement elem = evt.asStartElement();
					String name = elem.getName().getLocalPart();
					if ("rule".equals(name)) {
						if (tail == null)
							head = tail = new AccessControlRule();
						else
							tail = tail.next = new AccessControlRule();
						parseRule(reader, elem, tail);
					} else {
						throw new XMLStreamException("unknown tag " + name);
					}
				} else if (evt.isEndElement()) {
					if ("acl".equals(evt.asEndElement().getName()
							.getLocalPart()))
						break;
				}
			}
			return head;
		} finally {
			stream.close();
		}
	}

	static void parseRule(XMLEventReader reader, StartElement elem,
			AccessControlRule rule) throws XMLStreamException {
		Attribute pattern = elem.getAttributeByName(new QName("pattern"));
		if (pattern == null)
			throw new XMLStreamException("attribute pattern is required");
		rule.pattern = Pattern.compile(pattern.getValue());
		if (logger.isDebugEnabled())
			logger.trace("[parseRule] add rule for " + pattern.getValue());

		AccessControlRuleItem item = null;
		// accept|forward|redirect|error
		while (true) {
			XMLEvent evt = reader.nextTag();
			if (evt.isStartElement()) {
				elem = evt.asStartElement();
				String name = elem.getName().getLocalPart();
				Attribute role = elem.getAttributeByName(new QName("role"));
				Attribute state = elem.getAttributeByName(new QName("state"));
				Attribute path = elem.getAttributeByName(new QName("path"));
				Attribute code = elem.getAttributeByName(new QName("code"));
				Attribute inv = elem
						.getAttributeByName(new QName("invalidate"));
				if (logger.isDebugEnabled()) {
					logger.debug("[parseRule] "
							+ name
							+ " role = "
							+ (role == null ? "*" : StringUtils.defaultIfEmpty(
									role.getValue(), "*"))
							+ " state = "
							+ (state == null ? "*" : StringUtils
									.defaultIfEmpty(state.getValue(), "*"))
							+ (path == null ? "" : " path = " + path.getValue())
							+ (code == null ? "" : " code = " + code.getValue())
							+ (inv == null ? "" : " invalidate = "
									+ inv.getValue()));
				}

				if (item == null)
					rule.item = item = new AccessControlRuleItem();
				else
					item = item.next = new AccessControlRuleItem();
				item.roles = Tutor.enumValues(
						role == null ? null : role.getValue(), UserRole.class);
				item.states = Tutor.enumValues(
						state == null ? null : state.getValue(),
						UserState.class);

				if ("accept".equals(name)) {
					item.path = null;
				} else if ("forward".equals(name)) {
					if (path == null)
						throw new XMLStreamException(
								"attribute path is required");
					item.forward = true;
					item.path = path.getValue();
					item.invalidate = inv != null
							&& "true".equals(inv.getValue());
				} else if ("redirect".equals(name)) {
					if (path == null)
						throw new XMLStreamException(
								"attribute path is required");
					item.forward = false;
					item.path = path.getValue();
					item.invalidate = inv != null
							&& "true".equals(inv.getValue());
				} else if ("error".equals(name)) {
					if (code == null)
						throw new XMLStreamException(
								"attribute code is required");
					item.errorCode = Integer.parseInt(code.getValue());
				}
			} else if (evt.isEndElement()) {
				if ("rule".equals(evt.asEndElement().getName().getLocalPart()))
					break;
			}
		}
	}

	AccessControlRule acl;
	Map<String, AccessControlRule> ruleMap;

	@Override
	public void init(FilterConfig config) throws ServletException {
		if (logger.isTraceEnabled())
			logger.trace("[init] called");

		String sAcl = StringUtils.defaultString(config.getInitParameter(ACL),
				Tutor.DEFAULT_ACL_PATH);
		if (logger.isDebugEnabled())
			logger.debug("[init] ACL = " + sAcl);

		if (StringUtils.isNotEmpty(sAcl)) {
			try {
				acl = parseAcl(Tutor.getRealPath(sAcl));
			} catch (IOException e) {
				logger.error("[init] parse acl error", e);
				throw new ServletException(e);
			} catch (XMLStreamException e) {
				logger.error("[init] parse acl error", e);
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
		UserState state = UserState.Unavailable;
		UserModel user = SessionContainer.getLoginUser(request);
		if (user != null) {
			role = user.getRole();
			state = user.getState();
		}
		if (logger.isDebugEnabled())
			logger.debug("[doFilter] reqPath = " + reqPath + ", role = " + role
					+ ", state = " + state);
		// check with ACL
		if (acl != null) {
			AccessControlRule rule;
			if (ruleMap.containsKey(reqPath)) {
				rule = ruleMap.get(reqPath);
			} else {
				rule = acl;
				while (rule != null && !rule.match(reqPath))
					rule = rule.next;
				ruleMap.put(reqPath, rule);
			}
			AccessControlRuleItem item;
			if (rule != null && (item = rule.accept(role, state)) != null) {
				if (logger.isDebugEnabled())
					logger.debug("[doFilter] matched: " + rule + " " + item);
				if (item.errorCode != 0) {
					// send error code
					response.sendError(item.errorCode);
					return;
				}
				if (item.path != null && !item.path.equals(reqPath)) {
					// invalidate
					if (item.invalidate) {
						HttpSession session = request.getSession(false);
						if (session != null)
							session.invalidate();
					}
					// goto the specified page
					String path = getForwardPath(item.path,
							request.getRequestURI(), request.getQueryString());
					if (item.forward)
						request.getRequestDispatcher(path).forward(req, resp);
					else
						response.sendRedirect(request.getContextPath() + path);
					return;
				}
			}
		}
		// accept
		if (logger.isDebugEnabled())
			logger.debug("[doFilter] accepted.");
		chain.doFilter(req, resp);
	}

	private String getForwardPath(String path, String uri, String qstr)
			throws UnsupportedEncodingException {
		String url = StringUtils.isEmpty(qstr) ? uri : uri + "?" + qstr;
		return path.replace("{url}",
				URLEncoder.encode(url, Tutor.DEFAULT_ENCODING));
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
		AccessControlRuleItem item;
		AccessControlRule next;

		public boolean match(String path) {
			return pattern == null || pattern.matcher(path).matches();
		}

		public AccessControlRuleItem accept(UserRole role, UserState state) {
			for (AccessControlRuleItem item = this.item; item != null; item = item.next) {
				if (ArrayUtils.contains(item.roles, role)
						&& ArrayUtils.contains(item.states, state))
					return item;
			}
			return null;
		}

		@Override
		public String toString() {
			return pattern.pattern();
		}

	}

	/**
	 * <p>
	 * 访问控制规则。
	 * </p>
	 * 
	 * @author ren
	 * 
	 */
	static class AccessControlRuleItem {

		UserRole[] roles;
		UserState[] states;
		String path;
		boolean forward;
		int errorCode;
		boolean invalidate;
		AccessControlRuleItem next;

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			if (path == null)
				sb.append(errorCode == 0 ? "ACCEPT " : "ERROR " + errorCode);
			else if (forward)
				sb.append("FORAWRD ").append(path);
			else
				sb.append("REDIRECT ").append(path);
			return sb.toString();
		}

	}

}
