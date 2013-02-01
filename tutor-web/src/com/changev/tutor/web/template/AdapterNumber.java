package com.changev.tutor.web.template;

import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;

public class AdapterNumber extends AdapterModel<Number> implements
		TemplateNumberModel, TemplateScalarModel {

	public AdapterNumber(Number value) {
		super(value);
	}

	@Override
	public Number getAsNumber() throws TemplateModelException {
		return value;
	}

	@Override
	public String getAsString() throws TemplateModelException {
		return value.toString();
	}

}