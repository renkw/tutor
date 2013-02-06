/*
 * File   InstanceofMethodModel.java
 * Create 2013/02/06
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.template;

import java.util.List;

import org.apache.commons.lang.ClassUtils;


import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * @author ren
 * 
 */
public class InstanceofMethodModel implements TemplateMethodModelEx {

	@SuppressWarnings("rawtypes")
	@Override
	public Object exec(List arguments) throws TemplateModelException {
		// check correct
		if (arguments.size() != 2)
			throw new TemplateModelException(
					"usage: instanceof(value, className)");
		// parameters
		Object value = TemplateUtils.getAsObject(arguments.get(0));
		String clsName = TemplateUtils.getAsString(arguments.get(1));
		try {
			return ClassUtils.getClass(clsName).isInstance(value);
		} catch (ClassNotFoundException e) {
			throw new TemplateModelException(e);
		}
	}

}
