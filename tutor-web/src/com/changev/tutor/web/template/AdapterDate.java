package com.changev.tutor.web.template;

import java.util.Date;

import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

public class AdapterDate extends AdapterModel<Date> implements
		TemplateDateModel, TemplateScalarModel {

	public AdapterDate(Date value) {
		super(value);
	}

	@Override
	public Date getAsDate() throws TemplateModelException {
		return value;
	}

	@Override
	public int getDateType() {
		if (value instanceof java.sql.Date)
			return DATE;
		if (value instanceof java.sql.Time)
			return TIME;
		return DATETIME;
	}

	@Override
	public String getAsString() throws TemplateModelException {
		return value.toString();
	}

}