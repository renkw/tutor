/*
 * File   ParamRequiredValidator.java
 * Create 2013/01/14
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.util;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * 必须验证。
 * </p>
 * 
 * @author ren
 * 
 */
public class ParamRequiredValidator extends ParamValidator {

	@Override
	protected boolean validate(String v) {
		return StringUtils.isNotEmpty(v);
	}

	@Override
	protected boolean validate(String[] v) {
		if (v != null && v.length != 0) {
			for (int i = 0; i < v.length; i++) {
				if (StringUtils.isNotEmpty(v[i]))
					return true;
			}
		}
		return false;
	}

}
