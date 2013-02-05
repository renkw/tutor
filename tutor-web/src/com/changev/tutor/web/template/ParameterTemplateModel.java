/*
 * File   ParameterTemplateModel.java
 * Create 2013/01/31
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.template;

import java.util.HashMap;
import java.util.Map;

import freemarker.template.SimpleCollection;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import freemarker.template.TemplateSequenceModel;

/**
 * @author ren
 * 
 */
public class ParameterTemplateModel extends AdapterModel<Map<String, String[]>>
		implements TemplateHashModelEx {

	private Map<String, Param> paramMap;
	private TemplateCollectionModel keys;
	private TemplateCollectionModel values;

	public ParameterTemplateModel(Map<String, String[]> paramMap) {
		super(paramMap);
		this.paramMap = new HashMap<String, Param>(paramMap.size());
		for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
			this.paramMap.put(entry.getKey(), new Param(entry.getValue()));
		}
	}

	@Override
	public TemplateModel get(String key) throws TemplateModelException {
		return paramMap.get(key);
	}

	@Override
	public boolean isEmpty() throws TemplateModelException {
		return paramMap.isEmpty();
	}

	@Override
	public int size() throws TemplateModelException {
		return paramMap.size();
	}

	@Override
	public TemplateCollectionModel keys() throws TemplateModelException {
		if (keys == null)
			keys = new SimpleCollection(paramMap.keySet());
		return keys;
	}

	@Override
	public TemplateCollectionModel values() throws TemplateModelException {
		if (values == null)
			values = new SimpleCollection(paramMap.values());
		return values;
	}

	class Param extends AdapterModel<String[]> implements
			TemplateSequenceModel, TemplateScalarModel {

		private TemplateScalarModel[] values;

		public Param(String[] values) {
			super(values);
			this.values = new TemplateScalarModel[values.length];
			for (int i = 0; i < values.length; i++)
				this.values[i] = new SimpleScalar(values[i]);
		}

		@Override
		public String getAsString() throws TemplateModelException {
			return values[0].getAsString();
		}

		@Override
		public TemplateModel get(int index) throws TemplateModelException {
			if (index < 0 || index >= values.length)
				return null;
			return values[index];
		}

		@Override
		public int size() throws TemplateModelException {
			return values.length;
		}

	}

}
