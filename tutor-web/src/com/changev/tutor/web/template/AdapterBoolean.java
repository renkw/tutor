package com.changev.tutor.web.template;

import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

public class AdapterBoolean extends AdapterModel<Boolean> implements
		TemplateBooleanModel, TemplateScalarModel {

	public static final AdapterBoolean TRUE = new AdapterBoolean(Boolean.TRUE);
	public static final AdapterBoolean FALSE = new AdapterBoolean(Boolean.FALSE);

	public AdapterBoolean(Boolean value) {
		super(value);
	}

	@Override
	public boolean getAsBoolean() throws TemplateModelException {
		return value;
	}

	@Override
	public String getAsString() throws TemplateModelException {
		return value.toString();
	}

}