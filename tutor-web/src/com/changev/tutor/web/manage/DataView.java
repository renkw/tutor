/*
 * File   DataView.java
 * Create 2012/12/28
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.manage;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
import com.changev.tutor.web.View;
import com.db4o.ObjectSet;
import com.db4o.query.Constraint;
import com.db4o.query.Query;

/**
 * <p>
 * 数据管理。
 * </p>
 * 
 * @author ren
 * 
 */
public class DataView implements View {

	private static final Logger logger = Logger.getLogger(DataView.class);

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
		if (StringUtils.isNotEmpty(request.getParameter("search")))
			return search(request, response);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[postRender] called");
	}

	protected boolean search(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[search] called");

		String sType = request.getParameter("type");
		String sDeleted = request.getParameter("deleted");
		String sCreateFrom = request.getParameter("createFrom");
		String sCreateTo = request.getParameter("createTo");
		String sUpdateFrom = request.getParameter("updateFrom");
		String sUpdateTo = request.getParameter("updateTo");
		if (logger.isDebugEnabled()) {
			logger.debug("type = " + sType);
			logger.debug("deleted = " + sDeleted);
			logger.debug("createFrom = " + sCreateFrom);
			logger.debug("createTo = " + sCreateTo);
			logger.debug("updateFrom = " + sUpdateFrom);
			logger.debug("updateTo = " + sUpdateTo);
		}
		// TODO complex search
		Class<?> type = Class.forName("com.changev.tutor.model." + sType);
		Boolean deleted = StringUtils.isEmpty(sDeleted) ? null : Boolean
				.valueOf(sDeleted);
		Timestamp createFrom = StringUtils.isEmpty(sCreateFrom) ? null
				: Timestamp.valueOf(sCreateFrom);
		Timestamp createTo = StringUtils.isEmpty(sCreateTo) ? null : Timestamp
				.valueOf(sCreateTo);
		Timestamp updateFrom = StringUtils.isEmpty(sUpdateFrom) ? null
				: Timestamp.valueOf(sUpdateFrom);
		Timestamp updateTo = StringUtils.isEmpty(sUpdateTo) ? null : Timestamp
				.valueOf(sUpdateTo);
		// get all field names
		List<String> names = new ArrayList<String>();
		do {
			Field[] fields = type.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				if (Modifier.isStatic(fields[i].getModifiers())
						|| Modifier.isTransient(fields[i].getModifiers()))
					continue;
				names.add(fields[i].getName());
			}
		} while ((type = type.getSuperclass()) != Object.class);
		// make query
		Query query = Tutor.getCurrentContainer().query();
		Constraint con = query.constrain(type);
		if (deleted != null)
			con = query.descend("deleted").constrain(deleted).equal();
		if (createFrom != null)
			con = con.and(query.descend("createDateTime").constrain(createFrom)
					.greater().equal());
		if (createTo != null)
			con = con.and(query.descend("createDateTime").constrain(createTo)
					.smaller().equal());
		if (updateFrom != null)
			con = con.and(query.descend("updateDateTime").constrain(updateFrom)
					.greater().equal());
		if (updateTo != null)
			con = con.and(query.descend("updateDateTime").constrain(updateTo)
					.smaller().equal());
		ObjectSet<?> rows = query.execute();
		// set result
		Result result = new Result();
		result.setNames(names);
		result.setRows(rows);
		request.setAttribute("result", result);
		return true;
	}

	public static class Result {

		private List<String> names;
		private ObjectSet<?> rows;

		/**
		 * @return the names
		 */
		public List<String> getNames() {
			return names;
		}

		/**
		 * @param names
		 *            the names to set
		 */
		public void setNames(List<String> names) {
			this.names = names;
		}

		/**
		 * @return the rows
		 */
		public ObjectSet<?> getRows() {
			return rows;
		}

		/**
		 * @param rows
		 *            the rows to set
		 */
		public void setRows(ObjectSet<?> rows) {
			this.rows = rows;
		}

	}

}
