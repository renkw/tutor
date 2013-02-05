/*
 * File   ActionMessageDirectiveModel.java
 * Create 2013/01/31
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.template;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.changev.tutor.web.SessionContainer;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * @author ren
 * 
 */
public class ActionMessageDirectiveModel implements TemplateDirectiveModel {

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		// get message
		HttpServletRequest request = TemplateUtils.getRequest(env);
		String msg = SessionContainer.get(request).getActionMessage();
		// write message
		if (StringUtils.isNotEmpty(msg)) {
			Writer out = env.getOut();
			msg = StringEscapeUtils.escapeHtml(msg);
			if (body != null) {
				if (loopVars.length > 0)
					loopVars[0] = new SimpleScalar(msg);
				body.render(out);
			} else {
				writeDefault(out, msg);
			}
		}
	}

	protected void writeDefault(Writer out, String msg) throws IOException {
		out.append(
				"<div class=\"box-subtitle ui-state-highlight\"><div class=\"ui-icon ui-icon-comment float-left\"></div>")
				.append(msg).write("<br class=\"clear-left\" /></div>");
	}

}
