/*
 * File   ParamRequiredValidator.java
 * Create 2013/01/14
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.util;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * 正则表达式验证。
 * </p>
 * 
 * @author ren
 * 
 */
public class ParamPatternValidator extends ParamValidator {

	@Override
	protected boolean validate(String v) {
		return StringUtils.isEmpty(v) || v.matches(getPattern());
	}

	/**
	 * @return 正则表达式
	 */
	public String getPattern() {
		return (String) get("pattern");
	}

	/**
	 * @param pattern
	 *            正则表达式
	 */
	public void setPattern(String pattern) {
		set("pattern", pattern);
	}

}
