/*
 * File   JoinMethodModel.java
 * Create 2013/02/01
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.template;

import java.lang.reflect.Array;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * @author ren
 * 
 */
public class JoinMethodModel implements TemplateMethodModelEx {

	@SuppressWarnings("rawtypes")
	@Override
	public Object exec(List arguments) throws TemplateModelException {
		if (arguments.size() != 1 && arguments.size() != 2)
			throw new TemplateModelException("usage: join(arr[, delimiter])");
		Object arr = TemplateUtils.getAsObject(arguments.get(0));
		Object del = arguments.size() == 2 ? TemplateUtils
				.getAsString(arguments.get(1)) : ", ";
		StringBuilder sb = new StringBuilder();
		if (arr instanceof Iterable<?>) {
			for (Object e : (Iterable<?>) arr) {
				if (sb.length() > 0)
					sb.append(del);
				sb.append(ObjectUtils.toString(e));
			}
		} else if (arr.getClass().isArray()) {
			for (int i = 0, n = Array.getLength(arr); i < n; i++) {
				if (sb.length() > 0)
					sb.append(del);
				sb.append(ObjectUtils.toString(Array.get(arr, i)));
			}
		} else {
			sb.append(ObjectUtils.toString(arr));
		}
		return sb.toString();
	}

}
