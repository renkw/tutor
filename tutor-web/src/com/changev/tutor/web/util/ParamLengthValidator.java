/*
 * File   ParamLengthValidator.java
 * Create 2013/01/15
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.util;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * 输入参数长度验证。
 * </p>
 * 
 * @author ren
 * 
 */
public class ParamLengthValidator extends ParamValidator {

	@Override
	protected boolean validate(String v) {
		if (StringUtils.isNotEmpty(v)) {
			Integer len = getLength();
			if (len != null)
				return v.length() == len;
			Integer minlen = getMinLength();
			Integer maxlen = getMaxLength();
			if (minlen != null && maxlen != null)
				return v.length() >= minlen && v.length() <= maxlen;
			if (minlen != null)
				return v.length() >= minlen;
			if (maxlen != null)
				return v.length() <= maxlen;
		}
		return true;
	}

	/**
	 * @return 指定长度
	 */
	public Integer getLength() {
		return (Integer) get("length");
	}

	/**
	 * @param length
	 *            指定长度
	 */
	public void setLength(Integer length) {
		set("length", length);
	}

	/**
	 * @return 最大长度
	 */
	public Integer getMaxLength() {
		return (Integer) get("maxLength");
	}

	/**
	 * @param length
	 *            最大长度
	 */
	public void setMaxLength(Integer length) {
		set("maxLength", length);
	}

	/**
	 * @return 最小长度
	 */
	public Integer getMinLength() {
		return (Integer) get("minLength");
	}

	/**
	 * @param length
	 *            最小长度
	 */
	public void setMinLength(Integer length) {
		set("minLength", length);
	}

}
