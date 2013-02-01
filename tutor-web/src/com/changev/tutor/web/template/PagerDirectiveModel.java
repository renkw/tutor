/*
 * File   PagerDirectiveModel.java
 * Create 2013/01/31
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.template;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import freemarker.core.Environment;
import freemarker.template.SimpleNumber;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * @author ren
 * 
 */
public class PagerDirectiveModel implements TemplateDirectiveModel {

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		// parameters
		String pTotal = TemplateUtils.getAsString(params.get("total"), null);
		String pPage = TemplateUtils.getAsString(params.get("page"), null);
		String pShow = TemplateUtils.getAsString(params.get("show"), "10");
		// check correct
		if (StringUtils.isEmpty(pTotal) || StringUtils.isEmpty(pPage))
			throw new TemplateException(
					"usage: <@pager total=totalPages page=pageNo show=[10] [; page]>",
					env);
		// output
		final int total = Integer.parseInt(pTotal);
		final int show = Integer.parseInt(pShow);
		final int page = Math.min(Integer.parseInt(pPage), total);
		int start, end;
		if (page <= show) {
			start = 1;
			end = Math.min(show + 1, total);
		} else {
			start = (page - 1) / show * show;
			end = Math.min(start + show + 1, total);
		}
		Writer out = env.getOut();
		if (body == null) {
			writeDefault(out, total, page, start, end);
		} else {
			while (start <= end) {
				loopVars[0] = new SimpleNumber(start);
				body.render(out);
				start++;
			}
		}
	}

	protected void writeDefault(Writer out, final int total, final int page,
			int start, int end) throws IOException {
		if (total == 0) {
			out.write("&nbsp;没有记录&nbsp;");
			return;
		}
		
		if (start != 1) {
			out.write("<a href=\"#1\" title=\"第一页\"><span class=\"ui-icon ui-icon-seek-first\"></span></a>");
		}
		if (page > 1) {
			out.append("<a href=\"#")
					.append(Integer.toString(page - 1))
					.write("\" title=\"上一页\"><span class=\"ui-icon ui-icon-seek-prev\"></span></a>");
		}
		String sTotal = Integer.toString(total);
		while (start <= end) {
			String sPage = Integer.toString(start);
			out.write("<a href=\"#");
			if (start != page)
				out.write(sPage);
			out.append("\" title=\"第").append(sPage).append("/").append(sTotal)
					.write("页\"");
			if (start == page)
				out.write(" class=\"ui-state-active ui-corner-all\"");
			out.append(">").append(sPage).append("</a>");
			start++;
		}
		if (page < total) {
			out.append("<a href=\"#")
					.append(Integer.toString(page + 1))
					.write("\" title=\"下一页\"><span class=\"ui-icon ui-icon-seek-next\"></span></a>");
		}
		if (end != total) {
			out.append("<a href=\"#")
					.append(sTotal)
					.write("\" title=\"最后一页\"><span class=\"ui-icon ui-icon-seek-end\"></span></a>");
		}
	}

}
