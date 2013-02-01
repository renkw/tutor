/*
 * File   InputDirectiveModel.java
 * Create 2013/01/31
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.template;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;
import freemarker.template.TemplateSequenceModel;

/**
 * @author ren
 * 
 */
public class InputDirectiveModel implements TemplateDirectiveModel {

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		// parameters
		String pType = TemplateUtils.getAsString(params.get("type"), "text");
		String pName = TemplateUtils.getAsString(params.get("name"));
		String pSize = TemplateUtils.getAsString(params.get("size"));
		String pChecked = TemplateUtils
				.getAsString(params.get("checked"), null);
		String pRows = TemplateUtils.getAsString(params.get("rows"), "5");
		String pCols = TemplateUtils.getAsString(params.get("cols"), "40");
		String pId = TemplateUtils.getAsString(params.get("id"));
		String pClass = TemplateUtils.getAsString(params.get("class"));
		TemplateModel pOptions = (TemplateModel) params.get("options");
		TemplateModel pValue = (TemplateModel) params.get("value");
		// output
		boolean ahead = true;
		Writer out = env.getOut();
		if ("select".equals(pType)) { // select
			out.write("<select");
			writeAttr(out, "id", pId);
			writeAttr(out, "name", pName);
			writeAttr(out, "size", pSize);
			writeAttr(out, "class", pClass);
			out.write(">");
			if (body != null)
				body.render(out);
			if (pOptions instanceof TemplateHashModelEx) {
				TemplateHashModelEx options = (TemplateHashModelEx) pOptions;
				TemplateCollectionModel keys = options.keys();
				TemplateModelIterator iter = keys.iterator();
				while (iter.hasNext()) {
					TemplateModel key = iter.next();
					TemplateModel value = options.get(TemplateUtils
							.getAsString(key, ""));
					writeOption(out, key, value, pValue);
				}
			} else if (pOptions instanceof TemplateCollectionModel) {
				TemplateModelIterator iter = ((TemplateCollectionModel) pOptions)
						.iterator();
				while (iter.hasNext()) {
					TemplateModel value = iter.next();
					writeOption(out, value, value, pValue);
				}
			} else if (pOptions instanceof TemplateSequenceModel) {
				TemplateSequenceModel seq = (TemplateSequenceModel) pOptions;
				for (int i = 0; i < seq.size(); i++) {
					TemplateModel value = seq.get(i);
					writeOption(out, value, value, pValue);
				}
			}
			out.write("</select>");
			return;
		}
		if ("textarea".equals(pType)) { // textarea
			out.write("<textarea");
			writeAttr(out, "id", pId);
			writeAttr(out, "name", pName);
			writeAttr(out, "rows", pRows);
			writeAttr(out, "cols", pCols);
			writeAttr(out, "class", pClass);
			out.write(">");
			if (body != null) {
				body.render(out);
			} else {
				out.write(TemplateUtils.getAsString(pValue, ""));
			}
			out.write("</textarea>");
			return;
		}
		if ("checkbox".equals(pType) || "radio".equals(pType)) {
			if (pOptions instanceof TemplateHashModelEx) {
				TemplateHashModelEx options = (TemplateHashModelEx) pOptions;
				TemplateCollectionModel keys = options.keys();
				TemplateModelIterator iter = keys.iterator();
				while (iter.hasNext()) {
					TemplateModel key = iter.next();
					TemplateModel value = options.get(TemplateUtils
							.getAsString(key, ""));
					if (body != null) {
						if (loopVars.length > 1) {
							loopVars[0] = key;
							loopVars[1] = value;
						}
						body.render(out);
					} else {
						writeCheck(out, pType, key, pValue);
						writeAttr(out, "id", pId);
						writeAttr(out, "name", pName);
						writeAttr(out, "class", pClass);
						out.append(" />")
								.append(TemplateUtils.getAsString(value, ""))
								.append("</label>");
					}
				}
				return;
			}
			if (pOptions instanceof TemplateCollectionModel) {
				TemplateModelIterator iter = ((TemplateCollectionModel) pOptions)
						.iterator();
				while (iter.hasNext()) {
					TemplateModel value = iter.next();
					if (body != null) {
						if (loopVars.length > 1) {
							loopVars[0] = value;
							loopVars[1] = value;
						}
						body.render(out);
					} else {
						writeCheck(out, pType, value, pValue);
						writeAttr(out, "id", pId);
						writeAttr(out, "name", pName);
						writeAttr(out, "class", pClass);
						out.append(" />")
								.append(TemplateUtils.getAsString(value, ""))
								.append("</label>");
					}
				}
				return;
			}
			if (pOptions instanceof TemplateSequenceModel) {
				TemplateSequenceModel seq = (TemplateSequenceModel) pOptions;
				for (int i = 0; i < seq.size(); i++) {
					TemplateModel value = seq.get(i);
					if (body != null) {
						if (loopVars.length > 1) {
							loopVars[0] = value;
							loopVars[1] = value;
						}
						body.render(out);
					} else {
						writeCheck(out, pType, value, pValue);
						writeAttr(out, "id", pId);
						writeAttr(out, "name", pName);
						writeAttr(out, "class", pClass);
						out.append(" />")
								.append(TemplateUtils.getAsString(value, ""))
								.append("</label>");
					}
				}
				return;
			}
			ahead = false;
		}
		// default
		if (body != null) {
			out.write("<label>");
			if (ahead)
				body.render(out);
		}
		out.append("<input type=\"").append(pType).write("\"");
		writeAttr(out, "id", pId);
		writeAttr(out, "name", pName);
		writeAttr(out, "value", TemplateUtils.getAsString(pValue, null));
		writeAttr(out, "size", pSize);
		writeAttr(out, "class", pClass);
		if ("checked".equals(pChecked) || "true".equalsIgnoreCase(pChecked))
			out.append(" checked=\"checked\"");
		out.write(" />");
		if (body != null) {
			if (!ahead)
				body.render(out);
			out.write("</label>");
		}
	}

	protected void writeAttr(Writer out, String name, String value)
			throws IOException {
		if (StringUtils.isNotEmpty(value))
			out.append(" ").append(name).append("=\"")
					.append(StringEscapeUtils.escapeHtml(value)).write("\"");
	}

	protected void writeOption(Writer out, TemplateModel key,
			TemplateModel value, TemplateModel def) throws IOException,
			TemplateModelException {
		out.write("<option");
		writeAttr(out, "value", TemplateUtils.getAsString(key, ""));
		if (isSelected(def, key))
			out.write(" selected=\"selected\"");
		out.append(">")
				.append(StringEscapeUtils.escapeHtml(TemplateUtils.getAsString(
						value, ""))).write("</option>");
	}

	protected void writeCheck(Writer out, String type, TemplateModel value,
			TemplateModel def) throws IOException, TemplateModelException {
		out.write("<label><input");
		writeAttr(out, "type", type);
		writeAttr(out, "value", TemplateUtils.getAsString(value, ""));
		if (isSelected(def, value))
			out.append(" checked=\"checked\"");
	}

	protected boolean isSelected(TemplateModel collection, TemplateModel key)
			throws TemplateModelException {
		Object c = TemplateUtils.getAsObject(collection);
		Object k = TemplateUtils.getAsObject(key);
		if (c == null || k == null)
			return false;
		if (ObjectUtils.equals(c, k))
			return true;
		if (c instanceof Collection<?>)
			return ((Collection<?>) c).contains(k);
		if (c instanceof Map<?, ?>)
			return ((Map<?, ?>) c).containsKey(k);
		if (c.getClass().isArray()) {
			for (int i = 0, n = Array.getLength(c); i < n; i++) {
				if (ObjectUtils.equals(Array.get(c, i), k))
					return true;
			}
		}
		return ObjectUtils.equals(c.toString(), k.toString());
	}

}
