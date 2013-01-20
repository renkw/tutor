/*
 * File   DataEditorView.java
 * Create 2012/12/28
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.manage;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.util.ClassUtils;

import com.changev.tutor.Tutor;
import com.changev.tutor.model.AbstractModel;
import com.changev.tutor.web.View;
import com.db4o.activation.ActivationPurpose;

/**
 * <p>
 * 数据管理。
 * </p>
 * 
 * @author ren
 * 
 */
public class DataEditorView implements View {

	private static final Logger logger = Logger.getLogger(DataEditorView.class);

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
		if (StringUtils.isNotEmpty(request.getParameter("submit")))
			return submit(request, response);
		setEditorList(request);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[postRender] called");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected boolean submit(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[submit] called");

		AbstractModel data = null;

		String sId = request.getParameter("id");
		String sType = request.getParameter("type");
		if (logger.isTraceEnabled()) {
			logger.trace("[submit] id = " + sId);
			logger.trace("[submit] type = " + sType);
		}
		if (StringUtils.isNotEmpty(sId)) {
			data = Tutor.getCurrentContainerExt().getByID(Long.parseLong(sId));
		} else if (StringUtils.isNotEmpty(sType)) {
			data = (AbstractModel) Class.forName(sType).newInstance();
		}
		if (data != null) {
			if (logger.isDebugEnabled())
				logger.debug("[submit] id = " + sId);

			data.activate(ActivationPurpose.WRITE);
			Class<?> type = data.getClass();
			do {
				Field[] fields = type.getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					if (Modifier.isStatic(fields[i].getModifiers())
							|| Modifier.isTransient(fields[i].getModifiers()))
						continue;

					fields[i].setAccessible(true);
					String value = request.getParameter(fields[i].getName()
							+ "_NULL");
					if (Boolean.parseBoolean(value)) {
						fields[i].set(data, null);
						continue;
					}

					value = request.getParameter(fields[i].getName());
					if (value == null)
						continue;

					Class<?> fieldType = fields[i].getType();
					if (fieldType.isEnum()) {
						fields[i].set(data,
								Enum.valueOf((Class<Enum>) fieldType, value));
					} else if (fieldType == String.class) {
						fields[i].set(data, value);
					} else if (fieldType == boolean.class
							|| fieldType == Boolean.class) {
						fields[i].set(data, Boolean.valueOf(value));
					} else if (fieldType == byte.class
							|| fieldType == Byte.class) {
						fields[i].set(data, Byte.valueOf(value));
					} else if (fieldType == short.class
							|| fieldType == Short.class) {
						fields[i].set(data, Short.valueOf(value));
					} else if (fieldType == char.class
							|| fieldType == Character.class) {
						fields[i].set(data, value.charAt(0));
					} else if (fieldType == int.class
							|| fieldType == Integer.class) {
						fields[i].set(data, Integer.valueOf(value));
					} else if (fieldType == long.class
							|| fieldType == Long.class) {
						fields[i].set(data, Long.valueOf(value));
					} else if (fieldType == float.class
							|| fieldType == Float.class) {
						fields[i].set(data, Float.valueOf(value));
					} else if (fieldType == double.class
							|| fieldType == Double.class) {
						fields[i].set(data, Double.valueOf(value));
					} else if (Date.class.isAssignableFrom(fieldType)) {
						fields[i].set(
								data,
								DateUtils.parseDate(value, new String[] {
										Tutor.DEFAULT_DATETIME_FORMAT,
										Tutor.DEFAULT_DATE_FORMAT }));
					}
				}
			} while ((type = type.getSuperclass()) != Object.class);
			// store
			try {
				Tutor.getCurrentContainer().store(data);
				Tutor.commitCurrent();
			} catch (Throwable t) {
				Tutor.rollbackCurrent();
				throw t;
			}
		}
		// set editors
		setEditorList(request);
		return true;
	}

	protected void setEditorList(HttpServletRequest request) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[setEditorList] called");

		List<Editor> editors = new ArrayList<Editor>();
		Class<?> type = null;
		AbstractModel data = null;

		String sId = request.getParameter("id");
		String sType = request.getParameter("type");
		if (logger.isTraceEnabled()) {
			logger.trace("[setEditorList] id = " + sId);
			logger.trace("[setEditorList] type = " + sType);
		}
		if (StringUtils.isNotEmpty(sId)) {
			data = Tutor.getCurrentContainerExt().getByID(Long.parseLong(sId));
			data.activate(ActivationPurpose.READ);
			type = data.getClass();
		} else if (StringUtils.isNotEmpty(sType)) {
			type = Class.forName(sType);
		}

		request.setAttribute("type", type.getSimpleName());
		if (type != null) {
			do {
				Field[] fields = type.getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					if (Modifier.isStatic(fields[i].getModifiers())
							|| Modifier.isTransient(fields[i].getModifiers()))
						continue;

					String name = fields[i].getName();
					Class<?> fieldType = fields[i].getType();
					fields[i].setAccessible(true);
					Object value = data == null ? null : fields[i].get(data);

					StringBuilder editor = new StringBuilder(
							"<label><input type='checkbox' name='")
							.append(name)
							.append("_NULL' value='true' ")
							.append(data != null && value == null ? "checked='checked' />"
									: " />").append("NULL</label><br />");
					if (fieldType.isEnum()) {
						editor.append("<select name='").append(name)
								.append("'>");
						for (Object en : fieldType.getEnumConstants()) {
							editor.append("<option value='")
									.append(((Enum<?>) en).name())
									.append(en == value ? "' selected='selected'>"
											: "'>")
									.append(((Enum<?>) en).name())
									.append("</option>");
						}
						editor.append("</select>");
					} else if (fieldType == String.class) {
						String s = value == null ? "" : value.toString();
						editor.append("<textarea rows='2' cols='30' name='")
								.append(name).append("'>")
								.append(StringEscapeUtils.escapeHtml(s))
								.append("</textarea>");
					} else if (fieldType == boolean.class
							|| fieldType == Boolean.class) {
						editor.append(
								"<label><input type='checkbox' value='true' name='")
								.append(name)
								.append(Boolean.TRUE.equals(value) ? "' checked='checked' />"
										: "' />").append("TRUE</label>");
					} else if (ClassUtils.isPrimitiveOrWrapper(fieldType)
							|| Date.class.isAssignableFrom(fieldType)) {
						String s = value == null ? ""
								: value instanceof Date ? Tutor
										.formatDateTime((Date) value) : value
										.toString();
						editor.append("<input type='text' size='30' name='")
								.append(name).append("' value='")
								.append(StringEscapeUtils.escapeHtml(s))
								.append("' />");
					} else {
						continue;
					}
					editors.add(new Editor(name, editor.toString()));
				}
			} while ((type = type.getSuperclass()) != Object.class);
		}
		request.setAttribute("editors", editors);
	}

	public static class Editor {

		private String name;
		private String editor;

		public Editor(String name, String editor) {
			this.name = name;
			this.editor = editor;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the editor
		 */
		public String getEditor() {
			return editor;
		}

		/**
		 * @param editor
		 *            the editor to set
		 */
		public void setEditor(String editor) {
			this.editor = editor;
		}

	}
}
