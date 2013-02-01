/*
 * File   LinkMethodModel.java
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
public class LinkMethodModel implements TemplateMethodModel {

	@SuppressWarnings("rawtypes")
	@Override
	public Object exec(List arguments) throws TemplateModelException {
		// check correct
		if (arguments.size() != 1)
			throw new TemplateModelException("usage: link(url)");
		// add context path
		String url = (String) arguments.get(0);
		return url.startsWith("/") ? Tutor.getContextPath() + url : url;
	}

}
