/*
 * File   SampleView.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.front;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
import com.changev.tutor.web.View;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

/**
 * @author ren
 * 
 */
public class SampleView implements View {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(SampleView.class);

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
		ObjectContainer objc = Tutor.getCurrentContainer();
		ObjectSet<SimpleBean> set = objc.query(SimpleBean.class);
		SimpleBean bean = null;
		if (set.isEmpty()) {
			objc.store(bean = new SimpleBean());
			objc.commit();
		} else {
			bean = set.next();
		}
		request.setAttribute("user", bean);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[postRender] called");
	}

	public static class SimpleBean {

		private String name = "Foo";

		@Override
		public String toString() {
			return name;
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

	}
}
