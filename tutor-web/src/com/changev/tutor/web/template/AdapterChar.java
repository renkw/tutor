package com.changev.tutor.web.template;

import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;

public class AdapterChar extends AdapterModel<Character> implements
		TemplateNumberModel, TemplateScalarModel {

	public AdapterChar(Character value) {
		super(value);
	}

	@Override
	public Number getAsNumber() throws TemplateModelException {
		return Integer.valueOf(value.charValue());
	}

	@Override
	public String getAsString() throws TemplateModelException {
		return value.toString();
	}

}