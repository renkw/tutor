/*
 * File   NewQuestionView.java
 * Create 2013/01/29
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.front;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.ParentModel;
import com.changev.tutor.model.QuestionModel;
import com.changev.tutor.model.StudentModel;
import com.changev.tutor.model.UserModel;
import com.changev.tutor.web.SessionContainer;
import com.changev.tutor.web.View;
import com.changev.tutor.web.util.ParamUtils;
import com.changev.tutor.web.util.ParamValidator;
import com.changev.tutor.web.util.UserFileManager;
import com.google.gson.Gson;

/**
 * @author ren
 * 
 */
public class NewQuestionView implements View {

	private static final Logger logger = Logger
			.getLogger(NewQuestionView.class);

	private ParamValidator submitValidator;
	private String successPage;

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
		if (StringUtils.isNotEmpty(request.getParameter("submit")))
			return submit(request, response);
		setVariables(request, null);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[postRender] called");
		SessionContainer.get(request).setActionMessage(null);
	}

	protected boolean submit(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[submit] called");

		String subject = request.getParameter("subject");
		String title = request.getParameter("title");
		String apply = request.getParameter("apply");
		if (logger.isDebugEnabled()) {
			logger.debug("[submit] subject = " + subject);
			logger.debug("[submit] title = " + title);
			logger.debug("[submit] apply = " + apply);
		}

		UserModel loginUser = SessionContainer.getLoginUser(request);
		StudentModel student = null;
		if (loginUser instanceof StudentModel)
			student = (StudentModel) loginUser;
		else if (loginUser instanceof ParentModel)
			student = Tutor.one(((ParentModel) loginUser).getChildren());
		UserFileManager fm = Tutor.getBeanFactory().getBean(
				UserFileManager.class);
		// validation
		if (submitValidator == null || submitValidator.validate(request)) {
			if (logger.isDebugEnabled())
				logger.debug("[submit] validate passed");

			try {
				QuestionModel questionModel = new QuestionModel();
				questionModel.setQuestioner(loginUser);
				questionModel.setStudent(student);
				questionModel.setSubject(ParamUtils.emptyNull(subject));
				questionModel.setTitle(ParamUtils.emptyNull(title));
				if (StringUtils.isNotEmpty(apply) && student != null) {
					questionModel.setAssignTo(student.getDefaultAnswerer().get(
							subject));
				}
				for (Part part : request.getParts()) {
					if (part.getSize() == 0 || !"file".equals(part.getName()))
						continue;
					String header = part.getHeader("content-disposition");
					if (logger.isDebugEnabled())
						logger.debug("[submit] " + header);
					int i1 = header.indexOf("filename=\"") + 10;
					int i2 = header.indexOf('"', i1);
					String filename = header.substring(i1, i2);
					i1 = filename.lastIndexOf('.');
					String name = fm.create(loginUser.getEmail(), i1 == -1 ? ""
							: filename.substring(i1));
					OutputStream stream = fm.write(loginUser.getEmail(), name);
					try {
						IOUtils.copy(part.getInputStream(), stream);
						questionModel.getUploadPicturesFor().add(name);
					} finally {
						stream.close();
						part.delete();
					}
				}
				Tutor.getCurrentContainer().store(questionModel);
				Tutor.commitCurrent();
				SessionContainer.get(request).setActionMessage("发布提问成功。");
				SessionContainer.get(request).setQuestionList(null);
				if (StringUtils.isNotEmpty(successPage)) {
					if (logger.isDebugEnabled())
						logger.debug("[submit] success. goto " + successPage);
					response.sendRedirect(request.getContextPath()
							+ successPage);
					return false;
				}
			} catch (Throwable t) {
				Tutor.rollbackCurrent();
				throw t;
			}
		}

		setVariables(request, student);
		return true;
	}

	protected void setVariables(HttpServletRequest request, StudentModel student) {
		if (logger.isTraceEnabled())
			logger.trace("[setVariables] called");
		// subjects
		request.setAttribute("subjects", Tutor.getConstant("subjects"));
		// default answerer
		Map<String, String> defaultAnswerer = new HashMap<String, String>();
		if (student == null) {
			UserModel loginUser = SessionContainer.getLoginUser(request);
			if (loginUser instanceof StudentModel)
				student = (StudentModel) loginUser;
			else if (loginUser instanceof ParentModel)
				student = Tutor.one(((ParentModel) loginUser).getChildren());
		}
		if (student != null) {
			for (Map.Entry<String, UserModel> entry : student
					.getDefaultAnswerer().entrySet()) {
				defaultAnswerer.put(entry.getKey(), entry.getValue().getName());
			}
		}
		request.setAttribute("defaultAnswererJson", Tutor.getBeanFactory()
				.getBean(Gson.class).toJson(defaultAnswerer));
	}

	/**
	 * @return the submitValidator
	 */
	public ParamValidator getSubmitValidator() {
		return submitValidator;
	}

	/**
	 * @param submitValidator
	 *            the submitValidator to set
	 */
	public void setSubmitValidator(ParamValidator submitValidator) {
		this.submitValidator = submitValidator;
	}

	/**
	 * @return the successPage
	 */
	public String getSuccessPage() {
		return successPage;
	}

	/**
	 * @param successPage
	 *            the successPage to set
	 */
	public void setSuccessPage(String successPage) {
		this.successPage = successPage;
	}

}
