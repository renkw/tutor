/*
 * File   ParamCheckCodeValidator.java
 * Create 2013/01/14
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.changev.tutor.web.Messages;
import com.changev.tutor.web.SessionContainer;

/**
 * <p>
 * 输入验证码。
 * </p>
 * 
 * @author ren
 * 
 */
public class ParamCheckCodeValidator extends ParamValidator {

	@Override
	public boolean validate(HttpServletRequest request, Messages msg) {
		String v = request.getParameter(getName());
		String code = SessionContainer.get(request).getCheckCode();
		return addError(StringUtils.isNotEmpty(v) && v.equals(code), msg);
	}

}
