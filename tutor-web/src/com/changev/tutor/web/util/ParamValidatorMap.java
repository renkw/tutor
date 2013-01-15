/*
 * File   ParamValidatorMap.java
 * Create 2013/01/15
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.changev.tutor.web.Messages;

/**
 * @author ren
 * 
 */
public class ParamValidatorMap extends ParamValidator implements
		Map<String, Object> {

	private String prefix = "";
	private List<ParamValidator> list = new ArrayList<ParamValidator>();

	public ParamValidatorMap() {
		this.args = new HashMap<String, Object>();
	}

	private ParamValidatorMap(ParamValidatorMap parent, String prefix) {
		this.args = parent.args;
		this.prefix = prefix;
	}

	@Override
	public boolean validate(HttpServletRequest request, Messages msg) {
		boolean ret = true;
		String name = null;
		boolean failed = false;
		for (int i = 0; i < list.size(); i++) {
			ParamValidator v = list.get(i);
			String vn = v.getName();
			if (failed && vn.equals(name))
				continue;
			name = vn;
			failed = !v.validate(request, msg);
			if (failed)
				ret = false;
		}
		return ret;
	}

	@Override
	public Object put(String key, Object value) {
		if (prefix.isEmpty()) {
			if (value instanceof ParamValidator) {
				list.add((ParamValidator) value);
			} else {
				int i1 = key.indexOf('.');
				if (key.indexOf('.', i1 + 1) != -1) {
					set(key, value);
				} else {
					boolean m = key.startsWith("[]", i1 - 2);
					ParamValidator v = ParamValidator.getValidator(key
							.substring(i1 + 1));
					v.args = new ParamValidatorMap(this, key + '.');
					v.setName(key.substring(0, m ? i1 - 2 : i1));
					v.setMultiple(m);
					v.setMessage((String) value);
					list.add(v);
				}
			}
		} else {
			set(prefix + key, value);
		}
		return null;
	}

	@Override
	public Object get(Object key) {
		return get(prefix + key);
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsKey(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> keySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<Object> values() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Map.Entry<String, Object>> entrySet() {
		throw new UnsupportedOperationException();
	}

}
