/*
 * File   NumberWordMethodModel.java
 * Create 2013/02/01
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.template;

import java.util.List;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * @author ren
 * 
 */
public class NumberWordMethodModel implements TemplateMethodModelEx {

	static final String[] NUM_WORD = { "零", "一", "二", "三", "四", "五", "六", "七",
			"八", "九", "十" };

	@SuppressWarnings("rawtypes")
	@Override
	public Object exec(List arguments) throws TemplateModelException {
		// check correct
		if (arguments.size() != 1)
			throw new TemplateModelException("usage: numberWord(num)");
		Number num = TemplateUtils.getAsNumber(arguments.get(0), null);
		return num == null ? "" : NUM_WORD[num.intValue() % NUM_WORD.length];
	}

}
