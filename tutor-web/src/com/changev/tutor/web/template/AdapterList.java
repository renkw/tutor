package com.changev.tutor.web.template;

import java.util.List;

import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;
import freemarker.template.TemplateSequenceModel;

public class AdapterList extends AdapterModel<List<?>> implements
		TemplateSequenceModel, TemplateCollectionModel {

	private ObjectWrapper wrapper;

	public AdapterList(List<?> value, ObjectWrapper wrapper) {
		super(value);
		this.wrapper = wrapper;
	}

	@Override
	public TemplateModel get(int index) throws TemplateModelException {
		return wrapper.wrap(index < 0 || index >= size() ? null : value
				.get(index));
	}

	@Override
	public int size() throws TemplateModelException {
		return value.size();
	}

	@Override
	public TemplateModelIterator iterator() throws TemplateModelException {
		return new AdapterIterator(value.iterator(), wrapper);
	}

}