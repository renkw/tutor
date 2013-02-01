/*
 * File   FaceDirectiveModel.java
 * Create 2013/01/31
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.template;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.OrganizationModel;
import com.changev.tutor.model.StudentModel;
import com.changev.tutor.model.TeacherModel;
import com.changev.tutor.model.UserModel;
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
public class FaceDirectiveModel implements TemplateDirectiveModel {

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		HttpServletRequest request = TemplateUtils.getRequest(env);
		// parameters
		UserModel user = TemplateUtils.getAsObject(params.get("user"),
				SessionContainer.getLoginUser(request));
		String width = TemplateUtils.getAsString(params.get("width"), null);
		String height = TemplateUtils.getAsString(params.get("height"), null);
		String male = TemplateUtils.getAsString(params.get("male"),
				"/images/male.png");
		String female = TemplateUtils.getAsString(params.get("female"),
				"/images/female.png");
		String noname = TemplateUtils.getAsString(params.get("noname"),
				"/images/noname.png");
		// get face picture
		String face = null;
		if (user != null) {
			switch (user.getRole()) {
			case Parent:
				face = female;
				break;
			case Student:
				face = ((StudentModel) user).getFacePicture();
				if (StringUtils.isEmpty(face))
					face = ((StudentModel) user).getMale() ? male : female;
				else
					face = "/files/" + Tutor.id(user) + "/" + face;
				break;
			case Organization:
				face = ((OrganizationModel) user).getLogoPicture();
				if (StringUtils.isNotEmpty(face))
					face = "/files/" + Tutor.id(user) + "/" + face;
				break;
			case Teacher:
				face = ((TeacherModel) user).getMale() ? male : female;
				break;
			default:
				break;
			}
		}
		face = StringUtils.defaultIfEmpty(face, noname);

		Writer out = env.getOut();
		if (body != null) {
			if (loopVars.length > 0)
				loopVars[0] = new SimpleScalar(request.getContextPath() + face);
			body.render(out);
		} else {
			out.write("<img src=\"");
			if (face.startsWith("/"))
				out.write(request.getContextPath());
			out.append(face).write("\" alt=\"头像\" border=\"0\"");
			if (StringUtils.isNotEmpty(width))
				out.append(" width=\"").append(width.toString()).write("\"");
			if (StringUtils.isNotEmpty(height))
				out.append(" height=\"").append(height.toString()).write("\"");
			out.write(" />");
		}

	}

}
