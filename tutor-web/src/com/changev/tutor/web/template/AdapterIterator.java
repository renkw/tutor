package com.changev.tutor.web.template;

import java.util.Iterator;

import org.apache.commons.collections.ResettableIterator;

import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;

public class AdapterIterator extends AdapterModel<Iterator<?>> implements
		TemplateModelIterator, TemplateCollectionModel {

	private ObjectWrapper wrapper;

	public AdapterIterator(Iterator<?> value, ObjectWrapper wrapper) {
		super(value);
		this.wrapper = wrapper;
	}

	@Override
	public TemplateModel next() throws TemplateModelException {
		return wrapper.wrap(value.next());
	}

	@Override
	public boolean hasNext() throws TemplateModelException {
		return value.hasNext();
	}

	@Override
	public TemplateModelIterator iterator() throws TemplateModelException {
		if (value instanceof ResettableIterator)
			((ResettableIterator) value).reset();
		return this;
	}

}