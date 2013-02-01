/*
 * File   JsonMethodModel.java
 * Create 2013/01/31
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.template;

import java.util.List;

import com.changev.tutor.Tutor;
import com.google.gson.Gson;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;

/**
 * @author ren
 * 
 */
public class JsonMethodModel implements TemplateMethodModelEx {

	@SuppressWarnings("rawtypes")
	@Override
	public Object exec(List arguments) throws TemplateModelException {
		// check correct
		if (arguments.size() != 1)
			throw new TemplateModelException("usage: json(obj)");
		// to json
		return Tutor.getBeanFactory().getBean(Gson.class)
				.toJson(DeepUnwrap.unwrap((TemplateModel) arguments.get(0)));
	}

}
