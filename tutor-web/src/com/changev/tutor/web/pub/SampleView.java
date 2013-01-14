/*
 * File   SampleView.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.pub;

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

	private static final Logger logger = Logger.getLogger(SampleView.class);

	@Override
	public boolean preRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[preRender] called");
		ObjectContainer objc = Tutor.getCurrentContainer();
		ObjectSet<SimpleBean> set = objc.query(SimpleBean.class);
		if (set.isEmpty()) {
			objc.store(new SimpleBean("Foo", 0, 0));
			objc.store(new SimpleBean("Foo", 1, 1));
			objc.store(new SimpleBean("Bar", 0, 0));
			objc.store(new SimpleBean("Bar", 1, 1));
			objc.commit();
		}

		set = objc.queryByExample(new SimpleBean("Foo", null, 0));
		System.out.println("----------------" + set.size());
		SimpleBean bean1 = set.next();
		request.setAttribute("user", bean1);

		set = objc.queryByExample(new SimpleBean("Foo", null, 1));
		System.out.println("----------------" + set.size());
		SimpleBean bean2 = set.next();
		request.setAttribute("user", bean2);
		return true;
	}

	@Override
	public void postRender(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		if (logger.isTraceEnabled())
			logger.trace("[postRender] called");
	}

	public static class SimpleBean {

		private String name;
		private Integer wrapper;
		private int primitive;

		public SimpleBean(String name, Integer wrapper, int primitive) {
			this.name = name;
			this.wrapper = wrapper;
			this.primitive = primitive;
		}

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

		/**
		 * @return the wrapper
		 */
		public Integer getWrapper() {
			return wrapper;
		}

		/**
		 * @param wrapper
		 *            the wrapper to set
		 */
		public void setWrapper(Integer wrapper) {
			this.wrapper = wrapper;
		}

		/**
		 * @return the primitive
		 */
		public int getPrimitive() {
			return primitive;
		}

		/**
		 * @param primitive
		 *            the primitive to set
		 */
		public void setPrimitive(int primitive) {
			this.primitive = primitive;
		}

	}
}
