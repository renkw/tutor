/*
 * File   ParamDateValidator.java
 * Create 2013/01/19
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.changev.tutor.Tutor;
import com.changev.tutor.util.TextPattern;
import com.changev.tutor.web.Messages;

/**
 * <p>
 * 日期验证。
 * </p>
 * 
 * @author ren
 * 
 */
public class ParamDateValidator extends ParamValidator {

	private TextPattern namePattern;

	@Override
	public boolean validate(HttpServletRequest request, Messages msg) {
		if (namePattern == null)
			return super.validate(request, msg);

		String[] values = request.getParameterValues(getName());
		String[] names = namePattern.getVarNames();
		Map<String, String> vars = new HashMap<String, String>();
		for (int i = 0; i < names.length; i++) {
			if (StringUtils.isNumeric(names[i]))
				vars.put(names[i], values[i]);
			else
				vars.put(names[i], request.getParameter(names[i]));
		}
		return addError(validate(namePattern.toString(vars)), msg);
	}

	@Override
	protected boolean validate(String v) {
		return StringUtils.isEmpty(v)
				|| Tutor.parseDate(v, getFormat()) != null;
	}

	/**
	 * @return 参数组合样式
	 */
	public String getNamePattern() {
		return namePattern == null ? null : namePattern.toString();
	}

	/**
	 * @param pattern
	 *            参数组合样式
	 */
	public void setNamePattern(String pattern) {
		namePattern = TextPattern.parse(pattern);
	}

	/**
	 * @return 日期格式
	 */
	public String getFormat() {
		return StringUtils.defaultString((String) get("format"),
				Tutor.DEFAULT_DATETIME_FORMAT);
	}

	/**
	 * @param format
	 *            日期格式
	 */
	public void setFormat(String format) {
		set("format", format);
	}

}
