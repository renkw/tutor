package com.changev.tutor.web.template;

import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

public class AdapterString extends AdapterModel<Object> implements
		TemplateScalarModel {

	public AdapterString(Object value) {
		super(value);
	}

	@Override
	public String getAsString() throws TemplateModelException {
		return value.toString();
	}

}