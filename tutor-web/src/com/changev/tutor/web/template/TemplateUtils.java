/*
 * File   TemplateUtils.java
 * Create 2013/01/31
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.template;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.changev.tutor.Tutor;

import freemarker.core.Environment;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;

/**
 * @author ren
 * 
 */
public final class TemplateUtils {

	public static Object getAsObject(Object value)
			throws TemplateModelException {
		if (value instanceof TemplateModel)
			value = DeepUnwrap.unwrap((TemplateModel) value);
		return value;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getAsObject(Object value, T defaultValue)
			throws TemplateModelException {
		if (value instanceof TemplateModel)
			value = DeepUnwrap.unwrap((TemplateModel) value);
		return value == null ? defaultValue : (T) value;
	}

	public static String getAsString(Object value)
			throws TemplateModelException {
		return getAsString(value, "");
	}

	public static String getAsString(Object value, String defaultValue)
			throws TemplateModelException {
		value = getAsObject(value, null);
		return value == null ? defaultValue : value.toString();
	}

	public static Number getAsNumber(Object value)
			throws TemplateModelException {
		return getAsNumber(value, Integer.valueOf(0));
	}

	public static Number getAsNumber(Object value, Number defaultValue)
			throws TemplateModelException {
		value = getAsObject(value, null);
		if (value != null && !(value instanceof Number)) {
			try {
				value = NumberUtils.createNumber(value.toString());
			} catch (NumberFormatException e) {
				value = null;
			}
		}
		return value == null ? defaultValue : (Number) value;
	}

	public static Date getAsDate(Object value) throws TemplateModelException {
		return getAsDate(value, Tutor.currentDateTime());
	}

	public static Date getAsDate(Object value, Date defaultValue)
			throws TemplateModelException {
		value = getAsObject(value, defaultValue);
		if (value != null && !(value instanceof Date)) {
			String s = value.toString();
			try {
				if (s.indexOf(' ') != -1)
					value = java.sql.Timestamp.valueOf(s);
				else if (s.indexOf('-') != -1)
					value = java.sql.Date.valueOf(s);
				else if (s.indexOf(':') != -1)
					value = java.sql.Time.valueOf(s);
				else
					value = null;
			} catch (IllegalArgumentException e) {
				value = null;
			}
		}
		return value == null ? defaultValue : (Date) value;
	}

	public static boolean equals(Object obj1, Object obj2)
			throws TemplateModelException {
		return ObjectUtils.equals(getAsObject(obj1, null),
				getAsObject(obj2, null));
	}

	public static Class<?> getClass(Object obj) throws TemplateModelException {
		Object value = getAsObject(obj);
		return value == null ? Object.class : value.getClass();
	}

	public static HttpServletRequest getRequest(Environment env)
			throws TemplateModelException {
		return (HttpServletRequest) getAsObject(env.getVariable("request"));
	}

}
