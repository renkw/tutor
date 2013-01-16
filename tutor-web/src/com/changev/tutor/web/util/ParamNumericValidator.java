/*
 * File   ParamRequiredValidator.java
 * Create 2013/01/14
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.util;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * 数字输入验证。
 * </p>
 * 
 * @author ren
 * 
 */
public class ParamNumericValidator extends ParamValidator {

	@Override
	protected boolean validate(String v) {
		return StringUtils.isEmpty(v) || StringUtils.isNumeric(v);
	}

}
