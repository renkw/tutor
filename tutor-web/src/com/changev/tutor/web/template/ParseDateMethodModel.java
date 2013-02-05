/*
 * File   ParseDateMethodModel.java
 * Create 2013/01/31
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.template;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.changev.tutor.Tutor;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * @author ren
 * 
 */
public class ParseDateMethodModel implements TemplateMethodModelEx {

	private static final String[] FORMATS = { Tutor.DEFAULT_DATETIME_FORMAT,
			Tutor.DEFAULT_DATE_FORMAT, Tutor.DEFAULT_TIME_FORMAT };

	@SuppressWarnings("rawtypes")
	@Override
	public Object exec(List arguments) throws TemplateModelException {
		// check correct
		if (arguments.size() != 1 && arguments.size() != 2)
			throw new TemplateModelException(
					"usage: parseDate(dateStr[, format])");
		// parse
		Object value = TemplateUtils.getAsObject(arguments.get(0));
		if (value instanceof Date)
			return (Date) value;
		String str = value.toString();
		String format = arguments.size() == 2 ? TemplateUtils
				.getAsString(arguments.get(1)) : null;
		if (StringUtils.isEmpty(format)) {
			try {
				return DateUtils.parseDate(str, FORMATS);
			} catch (ParseException e) {
				return null;
			}
		}
		return Tutor.parseDate(str, format);
	}

}
