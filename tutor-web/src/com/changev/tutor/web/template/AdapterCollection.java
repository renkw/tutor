package com.changev.tutor.web.template;

import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;

public class AdapterCollection extends AdapterModel<Iterable<?>> implements
		TemplateCollectionModel {

	private ObjectWrapper wrapper;

	public AdapterCollection(Iterable<?> value, ObjectWrapper wrapper) {
		super(value);
		this.wrapper = wrapper;
	}

	@Override
	public TemplateModelIterator iterator() throws TemplateModelException {
		return new AdapterIterator(value.iterator(), wrapper);
	}

}