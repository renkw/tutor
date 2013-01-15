/*
 * File   ParamEqualsValidator.java
 * Create 2013/01/14
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.util;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.changev.tutor.web.Messages;

/**
 * <p>
 * 输入值存在验证。
 * </p>
 * 
 * @author ren
 * 
 */
public class ParamEqualsValidator extends ParamValidator {

	@Override
	public boolean validate(HttpServletRequest request, Messages msg) {
		String name = getName(), aName = getAnotherName();
		boolean v = multiple ? StringUtils.equals(request.getParameter(name),
				request.getParameter(aName)) : Arrays.equals(
				request.getParameterValues(name),
				request.getParameterValues(aName));
		return addError(v, msg);
	}

	/**
	 * @return 比较的参数名
	 */
	public String getAnotherName() {
		return (String) get("anotherName");
	}

	/**
	 * @param name
	 *            比较的参数名
	 */
	public void setAnotherName(String name) {
		set("anotherName", name);
	}

}
