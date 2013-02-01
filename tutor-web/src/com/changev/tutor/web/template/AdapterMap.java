package com.changev.tutor.web.template;

import java.util.Map;

import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;

public class AdapterMap extends AdapterModel<Map<?, ?>> implements
		TemplateHashModelEx, TemplateCollectionModel {

	private ObjectWrapper wrapper;

	public AdapterMap(Map<?, ?> value, ObjectWrapper wrapper) {
		super(value);
		this.wrapper = wrapper;
	}

	@Override
	public TemplateModel get(String key) throws TemplateModelException {
		return wrapper.wrap(value.get(key));
	}

	@Override
	public boolean isEmpty() throws TemplateModelException {
		return value.isEmpty();
	}

	@Override
	public int size() throws TemplateModelException {
		return value.size();
	}

	@Override
	public TemplateCollectionModel keys() throws TemplateModelException {
		return new AdapterCollection(value.keySet(), wrapper);
	}

	@Override
	public TemplateCollectionModel values() throws TemplateModelException {
		return new AdapterCollection(value.values(), wrapper);
	}

	@Override
	public TemplateModelIterator iterator() throws TemplateModelException {
		return new AdapterIterator(value.entrySet().iterator(), wrapper);
	}

}