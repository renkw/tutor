package com.changev.tutor.web.template;

import java.lang.reflect.Array;

import org.apache.commons.collections.IteratorUtils;

import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;
import freemarker.template.TemplateSequenceModel;

public class AdapterArray extends AdapterModel<Object> implements
		TemplateSequenceModel, TemplateCollectionModel {

	private ObjectWrapper wrapper;

	public AdapterArray(Object value, ObjectWrapper wrapper) {
		super(value);
		this.wrapper = wrapper;
	}

	@Override
	public TemplateModel get(int index) throws TemplateModelException {
		return wrapper.wrap(index < 0 || index >= size() ? null : Array.get(
				value, index));
	}

	@Override
	public int size() throws TemplateModelException {
		return Array.getLength(value);
	}

	@Override
	public TemplateModelIterator iterator() throws TemplateModelException {
		return new AdapterIterator(IteratorUtils.arrayIterator(value), wrapper);
	}

}