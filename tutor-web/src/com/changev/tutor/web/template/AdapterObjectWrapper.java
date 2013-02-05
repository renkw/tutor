/*
 * File   DelegateObjectWrapper.java
 * Create 2013/02/01
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.template;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.util.ModelFactory;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateModel;

/**
 * @author ren
 * 
 */
public class AdapterObjectWrapper extends BeansWrapper {

	@SuppressWarnings("rawtypes")
	@Override
	protected ModelFactory getModelFactory(Class clazz) {
		if (clazz == Boolean.class || clazz == boolean.class)
			return BOOLEAN;
		if (clazz == Character.class || clazz == char.class)
			return CHAR;
		if (CharSequence.class.isAssignableFrom(clazz) || clazz.isEnum())
			return STRING;
		if (Number.class.isAssignableFrom(clazz) || clazz.isPrimitive())
			return NUMBER;
		if (Date.class.isAssignableFrom(clazz))
			return DATE;
		if (clazz.isArray())
			return ARRAY;
		if (List.class.isAssignableFrom(clazz))
			return LIST;
		if (Iterable.class.isAssignableFrom(clazz))
			return COLLECTION;
		if (Map.class.isAssignableFrom(clazz))
			return MAP;
		if (Iterator.class.isAssignableFrom(clazz))
			return ITERATOR;
		return super.getModelFactory(clazz);
	}

	static final ModelFactory BOOLEAN = new ModelFactory() {
		@Override
		public TemplateModel create(Object object, ObjectWrapper wrapper) {
			return (Boolean) object ? AdapterBoolean.TRUE
					: AdapterBoolean.FALSE;
		}
	};

	static final ModelFactory CHAR = new ModelFactory() {
		@Override
		public TemplateModel create(Object object, ObjectWrapper wrapper) {
			return new AdapterChar((Character) object);
		}
	};

	static final ModelFactory NUMBER = new ModelFactory() {
		@Override
		public TemplateModel create(Object object, ObjectWrapper wrapper) {
			return new AdapterNumber((Number) object);
		}
	};

	static final ModelFactory DATE = new ModelFactory() {
		@Override
		public TemplateModel create(Object object, ObjectWrapper wrapper) {
			return new AdapterDate((Date) object);
		}
	};

	static final ModelFactory STRING = new ModelFactory() {
		@Override
		public TemplateModel create(Object object, ObjectWrapper wrapper) {
			return new AdapterString(object);
		}
	};

	static final ModelFactory ITERATOR = new ModelFactory() {
		@Override
		public TemplateModel create(Object object, ObjectWrapper wrapper) {
			return new AdapterIterator((Iterator<?>) object, wrapper);
		}
	};

	static final ModelFactory ARRAY = new ModelFactory() {
		@Override
		public TemplateModel create(Object object, ObjectWrapper wrapper) {
			return new AdapterArray(object, wrapper);
		}
	};

	static final ModelFactory LIST = new ModelFactory() {
		@Override
		public TemplateModel create(Object object, ObjectWrapper wrapper) {
			return new AdapterList((List<?>) object, wrapper);
		}
	};

	static final ModelFactory COLLECTION = new ModelFactory() {
		@Override
		public TemplateModel create(Object object, ObjectWrapper wrapper) {
			return new AdapterCollection((Iterable<?>) object, wrapper);
		}
	};

	static final ModelFactory MAP = new ModelFactory() {
		@Override
		public TemplateModel create(Object object, ObjectWrapper wrapper) {
			return new AdapterMap((Map<?, ?>) object, wrapper);
		}
	};

}
