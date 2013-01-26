/*
 * File   MemberView.java
 * Create 2013/01/25
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.front;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import com.changev.tutor.model.TeacherModel;
import com.changev.tutor.model.UserModel;
import com.changev.tutor.model.UserRole;
import com.changev.tutor.web.SessionContainer;
import com.changev.tutor.web.View;
import com.changev.tutor.web.util.UserFileManager;
import com.google.gson.Gson;

/**
 * <p>
 * 用户主页。
 * </p>
 * 
 * @author ren
 * 
 */
public class MemberView implements View {

	private static final Logger logger = Logger.getLogger(MemberView.class);

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
		if (StringUtils.isNotEmpty(request.getParameter("newq")))
			return newQuestion(request, response);
		setVariables(request);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[postRender] called");
	}

	protected boolean newQuestion(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[newQuestion] called");

		String student = request.getParameter("student");
		String subject = request.getParameter("subject");
		String answerer = request.getParameter("answerer");
		String title = request.getParameter("title");
		if (logger.isDebugEnabled()) {
			logger.debug("[newQuesiton] student = " + student);
			logger.debug("[newQuesiton] subject = " + subject);
			logger.debug("[newQuesiton] answerer = " + answerer);
			logger.debug("[newQuesiton] title = " + title);
		}

		UserModel loginUser = SessionContainer.getLoginUser(request);
		StudentModel studentModel = Tutor.fromId(student);
		UserFileManager fm = Tutor.getBeanFactory().getBean(
				UserFileManager.class);
		try {
			QuestionModel questionModel = new QuestionModel();
			questionModel.setQuestioner(loginUser);
			questionModel.setStudent(studentModel);
			questionModel.setSubject(Tutor.emptyNull(subject));
			questionModel
					.setAssignTo(studentModel.getDefaultAnswerer() == null ? null
							: studentModel.getDefaultAnswerer().get(subject));
			questionModel.setTitle(Tutor.emptyNull(title));
			for (Part part : request.getParts()) {
				if (part.getSize() == 0 || !"file".equals(part.getName()))
					continue;
				String header = part.getHeader("content-disposition");
				if (logger.isDebugEnabled())
					logger.debug("[newQuesiton] " + header);
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
		} catch (Throwable t) {
			Tutor.rollbackCurrent();
			throw t;
		}
		setVariables(request);
		return true;
	}

	protected void setVariables(HttpServletRequest request) {
		if (logger.isTraceEnabled())
			logger.trace("[setVariables] called");

		UserModel loginUser = SessionContainer.getLoginUser(request);
		List<StudentModel> studentList;
		List<QuestionModel> questionList;
		if (loginUser.getRole() == UserRole.Parent) {
			studentList = ((ParentModel) loginUser).getChildren();
			questionList = ((ParentModel) loginUser).getQuestions();
		} else {
			studentList = Arrays.asList((StudentModel) loginUser);
			questionList = ((StudentModel) loginUser).getQuestions();
		}
		questionList = Tutor.listDesc(questionList/* , 10 */);
		request.setAttribute("students", studentList);
		request.setAttribute("questions", questionList);
		request.setAttribute("subjects", Tutor.getConstant("subjects"));

		Map<String, String> answerers = new HashMap<String, String>();
		for (StudentModel student : studentList) {
			if (student.getDefaultAnswerer() == null
					|| student.getDefaultAnswerer().isEmpty())
				continue;
			String id = Tutor.id(student) + ".";
			for (Map.Entry<String, TeacherModel> entry : student
					.getDefaultAnswerer().entrySet()) {
				answerers.put(id + entry.getKey(), entry.getValue()
						.getFullName());
			}
		}
		request.setAttribute("answererJson",
				Tutor.getBeanFactory().getBean(Gson.class).toJson(answerers));
	}

}
