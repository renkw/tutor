/*
 * File   ParamContainsValidator.java
 * Create 2013/01/19
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.util;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * 验证参数值是否符合预定值。
 * </p>
 * 
 * @author ren
 * 
 */
public class ParamContainsValidator extends ParamValidator {

	private boolean ignoreCase;

	@Override
	protected boolean validate(String v) {
		if (StringUtils.isEmpty(v))
			return true;
		Object values = getValues();
		if (values == null)
			return false;
		if (values.getClass() == String.class)
			return contains(StringUtils.split((String) values, ','), v);
		if (values.getClass() == String[].class)
			return contains((String[]) values, v);
		if (values instanceof Collection)
			return contains((Collection<?>) values, v);
		if (values instanceof Map)
			return contains((Map<?, ?>) values, v);
		return false;
	}

	private boolean contains(String[] sa, String v) {
		for (int i = 0; i < sa.length; i++) {
			if (ignoreCase ? v.equalsIgnoreCase(sa[i]) : v.equals(sa[i]))
				return true;
		}
		return false;
	}

	private boolean contains(Collection<?> c, String v) {
		if (!ignoreCase)
			return c.contains(v);
		for (Object e : c) {
			if (e != null && v.equalsIgnoreCase(e.toString()))
				return true;
		}
		return false;
	}

	private boolean contains(Map<?, ?> map, String v) {
		if (!ignoreCase)
			return map.containsKey(v);
		return contains(map.keySet(), v);
	}

	/**
	 * @return 取得预设值
	 */
	public Object getValues() {
		return get("values");
	}

	/**
	 * @param values
	 *            设置预设值
	 */
	public void setValues(Object values) {
		set("values", values);
	}

	/**
	 * @return the ignoreCase
	 */
	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	/**
	 * @param ignoreCase
	 *            the ignoreCase to set
	 */
	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

}
