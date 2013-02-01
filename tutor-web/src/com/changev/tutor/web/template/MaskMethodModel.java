/*
 * File   MaskMethodModel.java
 * Create 2013/01/31
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.template;

import java.util.List;

import com.changev.tutor.Tutor;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * @author ren
 * 
 */
public class MaskMethodModel implements TemplateMethodModel {

	@SuppressWarnings("rawtypes")
	@Override
	public Object exec(List arguments) throws TemplateModelException {
		// check correct
		if (arguments.size() != 1 && arguments.size() != 2)
			throw new TemplateModelException("usage: mask(str[, off])");
		String value = (String) arguments.get(0);
		int offset = arguments.size() == 2 ? Integer
				.parseInt((String) arguments.get(1)) : 2;
		return Tutor.mask(value, offset);
	}

}
