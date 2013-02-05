/*
 * File   ParseNumberMethodModel.java
 * Create 2013/01/31
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.template;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.changev.tutor.Tutor;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * @author ren
 * 
 */
public class ParseNumberMethodModel implements TemplateMethodModelEx {

	@SuppressWarnings("rawtypes")
	@Override
	public Object exec(List arguments) throws TemplateModelException {
		// check correct
		if (arguments.size() != 1 && arguments.size() != 2)
			throw new TemplateModelException(
					"usage: parseNumber(numStr[, format])");
		// parse
		Object value = TemplateUtils.getAsObject(arguments.get(0));
		if (value instanceof Number)
			return (Number) value;
		String str = value.toString();
		String format = arguments.size() == 2 ? TemplateUtils
				.getAsString(arguments.get(1)) : null;
		if (StringUtils.isEmpty(format)) {
			try {
				return NumberUtils.createNumber(str);
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return Tutor.parseNumber(str, format);
	}

}
