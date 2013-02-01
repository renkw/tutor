/*
 * File   AdapterModel.java
 * Create 2013/02/01
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.template;

import org.apache.commons.lang.ObjectUtils;

import freemarker.template.AdapterTemplateModel;

/**
 * @author ren
 * 
 */
@SuppressWarnings("rawtypes")
public class AdapterModel<T> implements AdapterTemplateModel {

	protected final T value;

	public AdapterModel(T value) {
		this.value = value;
	}

	@Override
	public Object getAdaptedObject(Class hint) {
		return value;
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this || obj.getClass() == getClass()
				&& ObjectUtils.equals(((AdapterModel<?>) obj).value, value);
	}

	@Override
	public java.lang.String toString() {
		return value.toString();
	}

}
