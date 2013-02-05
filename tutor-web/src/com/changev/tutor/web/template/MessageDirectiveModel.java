/*
 * File   MessageDirectiveModel.java
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

import com.changev.tutor.web.Messages;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * <p>
 * 输出消息。
 * </p>
 * 
 * @author ren
 * @see Messages
 */
public class MessageDirectiveModel implements TemplateDirectiveModel {

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		// parameters
		String pType = TemplateUtils.getAsString(params.get("type"), "errors");
		String pName = TemplateUtils.getAsString(params.get("name"), null);
		// check correct
		if (StringUtils.isEmpty(pName))
			throw new TemplateException(
					"usage <@message type=[errors|messages|warnings] name=required [; msg]>",
					env);
		// get message
		HttpServletRequest request = TemplateUtils.getRequest(env);
		String msg;
		if ("messages".equals(pType))
			msg = Messages.get(request).getMessages().get(pName);
		else if ("warnings".equals(pType))
			msg = Messages.get(request).getWarnings().get(pName);
		else
			msg = Messages.get(request).getErrors().get(pName);
		// write message
		if (StringUtils.isNotEmpty(msg)) {
			Writer out = env.getOut();
			msg = StringEscapeUtils.escapeHtml(msg);
			if (body != null) {
				if (loopVars.length > 0)
					loopVars[0] = new SimpleScalar(msg);
				body.render(out);
			} else {
				writeDefault(out, pType, msg);
			}
		}
	}

	protected void writeDefault(Writer out, String type, String msg)
			throws IOException {
		if ("messages".equals(type)) {
			out.append(
					"<div class=\"ui-state-default\"><div class=\"ui-icon ui-icon-info float-left\"></div>")
					.append(msg).write("</div>");
		} else if ("warnings".equals(type)) {
			out.append(
					"<div class=\"ui-state-highlight\"><div class=\"ui-icon ui-icon-alert float-left\"></div>")
					.append(msg).write("</div>");
		} else {
			out.append(
					"<div class=\"ui-state-error\"><div class=\"ui-icon ui-icon-close float-left\"></div>")
					.append(msg).write("</div>");
		}
	}

}
