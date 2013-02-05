/*
 * File   MaskMethodModel.java
 * Create 2013/01/31
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.template;

import java.util.List;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * <p>
 * 把邮件地址的指定位置后面部分替换为*。 保留@后一个字符和域名后缀。
 * </p>
 * 
 * @author ren
 * 
 */
public class MaskEmailMethodModel implements TemplateMethodModel {

	@SuppressWarnings("rawtypes")
	@Override
	public Object exec(List arguments) throws TemplateModelException {
		// check correct
		if (arguments.size() != 1 && arguments.size() != 2)
			throw new TemplateModelException("usage: maskEmail(str[, off])");
		String value = (String) arguments.get(0);
		int offset = arguments.size() == 2 ? Integer
				.parseInt((String) arguments.get(1)) : 2;
		return maskEmail(value, offset);
	}

	private static String maskEmail(String s, int off) {
		char[] ca = s.toCharArray();
		int i1 = s.indexOf('@'), i2 = s.lastIndexOf('.');
		if (i2 == -1)
			i2 = s.length();
		if (i1 == -1)
			i1 = i2;
		while (off < i1)
			ca[off++] = '*';
		off += 2;
		while (off < i2)
			ca[off++] = '*';
		return new String(ca);
	}

}
