/*
 * File   IdMethodModel.java
 * Create 2013/01/31
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.template;

import java.util.List;

import com.changev.tutor.Tutor;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * <p>
 * 获取对象访问ID。
 * </p>
 * 
 * @author ren
 * 
 */
public class IdMethodModel implements TemplateMethodModelEx {

	@Override
	@SuppressWarnings("rawtypes")
	public Object exec(List arguments) throws TemplateModelException {
		// check correct
		if (arguments.size() != 1)
			throw new TemplateModelException("usage: id(model)");
		// return id
		return Tutor.id(TemplateUtils.getAsObject(arguments.get(0),
				Object.class));
	}

}
