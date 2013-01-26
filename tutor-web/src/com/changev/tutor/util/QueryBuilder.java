/*
 * File   QueryBuilder.java
 * Create 2013/01/25
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.util;

import org.apache.log4j.Logger;

import com.changev.tutor.Tutor;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Constraint;
import com.db4o.query.Evaluation;
import com.db4o.query.Query;

/**
 * @author ren
 * 
 */
public final class QueryBuilder<T> {

	private static final Logger performance = Logger
			.getLogger(Tutor.PERFORMANCE_LOGGER_NAME);

	public static interface SubQuery {
		void query(QueryBuilder<?> builder);
	}

	private Query query;
	private Constraint c;
	private boolean andOr;

	private QueryBuilder(Query query, boolean andOr) {
		this.query = query;
		this.andOr = andOr;
	}

	public QueryBuilder(ObjectContainer objc, Class<T> type) {
		this(objc.query(), true);
		query.constrain(type);
	}

	public QueryBuilder(Class<T> type) {
		this(Tutor.getCurrentContainer(), type);
	}

	@SuppressWarnings("unchecked")
	public QueryBuilder(T... gene) {
		this(Tutor.getCurrentContainer(), (Class<T>) gene.getClass()
				.getComponentType());
	}

	private Query descend(String[] names) {
		Query q = query;
		if (names != null) {
			for (int i = 0; i < names.length; i++)
				q = q.descend(names[i]);
		}
		return q;
	}

	public <TYPE> QueryBuilder<TYPE> at(String... names) {
		return new QueryBuilder<TYPE>(descend(names), andOr);
	}

	public QueryBuilder<T> and(SubQuery sub) {
		boolean andOr = this.andOr;
		Constraint c = this.c;
		this.andOr = true;
		this.c = null;
		sub.query(this);
		if (this.c != null)
			c = c == null ? this.c : andOr ? c.and(this.c) : c.or(this.c);
		this.c = c;
		this.andOr = andOr;
		return this;
	}

	public QueryBuilder<T> or(SubQuery sub) {
		boolean andOr = this.andOr;
		Constraint c = this.c;
		this.andOr = false;
		this.c = null;
		sub.query(this);
		if (this.c != null)
			c = c == null ? this.c : andOr ? c.and(this.c) : c.or(this.c);
		this.c = c;
		this.andOr = andOr;
		return this;
	}

	public QueryBuilder<T> not() {
		if (c != null)
			c = c.not();
		return this;
	}

	public QueryBuilder<T> isNull(String... names) {
		Constraint c1 = descend(names).constrain(null);
		c = c == null ? c1 : andOr ? c.and(c1) : c.or(c1);
		return this;
	}

	public QueryBuilder<T> notNull(String... names) {
		Constraint c1 = descend(names).constrain(null).not();
		c = c == null ? c1 : andOr ? c.and(c1) : c.or(c1);
		return this;
	}

	public QueryBuilder<T> isTrue(String... names) {
		Constraint c1 = descend(names).constrain(Boolean.TRUE);
		c = c == null ? c1 : andOr ? c.and(c1) : c.or(c1);
		return this;
	}

	public QueryBuilder<T> notTrue(String... names) {
		Constraint c1 = descend(names).constrain(Boolean.TRUE).not();
		c = c == null ? c1 : andOr ? c.and(c1) : c.or(c1);
		return this;
	}

	public QueryBuilder<T> isFalse(String... names) {
		Constraint c1 = descend(names).constrain(Boolean.FALSE);
		c = c == null ? c1 : andOr ? c.and(c1) : c.or(c1);
		return this;
	}

	public QueryBuilder<T> notFalse(String... names) {
		Constraint c1 = query.constrain(Boolean.FALSE).not();
		c = c == null ? c1 : andOr ? c.and(c1) : c.or(c1);
		return this;
	}

	public QueryBuilder<T> eq(Object value, String... names) {
		Constraint c1 = descend(names).constrain(value);
		c = c == null ? c1 : andOr ? c.and(c1) : c.or(c1);
		return this;
	}

	public QueryBuilder<T> ne(Object value, String... names) {
		Constraint c1 = descend(names).constrain(value).not();
		c = c == null ? c1 : andOr ? c.and(c1) : c.or(c1);
		return this;
	}

	public QueryBuilder<T> gt(Object value, String... names) {
		Constraint c1 = descend(names).constrain(value).greater();
		c = c == null ? c1 : andOr ? c.and(c1) : c.or(c1);
		return this;
	}

	public QueryBuilder<T> ge(Object value, String... names) {
		Constraint c1 = descend(names).constrain(value).greater().equal();
		c = c == null ? c1 : andOr ? c.and(c1) : c.or(c1);
		return this;
	}

	public QueryBuilder<T> lt(Object value, String... names) {
		Constraint c1 = descend(names).constrain(value).smaller();
		c = c == null ? c1 : andOr ? c.and(c1) : c.or(c1);
		return this;
	}

	public QueryBuilder<T> le(Object value, String... names) {
		Constraint c1 = descend(names).constrain(value).smaller().equal();
		c = c == null ? c1 : andOr ? c.and(c1) : c.or(c1);
		return this;
	}

	public QueryBuilder<T> range(Object from, Object to, String... names) {
		Query q = descend(names);
		Constraint c1 = q.constrain(from).greater().equal()
				.and(q.constrain(to).smaller().equal());
		c = c == null ? c1 : andOr ? c.and(c1) : c.or(c1);
		return this;
	}

	public QueryBuilder<T> in(Iterable<?> iter, String... names) {
		Constraint c1 = null;
		if (iter != null) {
			Query q = descend(names);
			for (Object v : iter)
				c1 = c1 == null ? q.constrain(v) : c1.or(q.constrain(v));
		}
		if (c1 != null)
			c = c == null ? c1 : andOr ? c.and(c1) : c.or(c1);
		return this;
	}

	// must called before all other conditions, reason unknown
	public QueryBuilder<T> eval(Evaluation e, String... names) {
		descend(names).constrain(e);
		return this;
	}

	public QueryBuilder<T> asc(String... names) {
		descend(names).orderAscending();
		return this;
	}

	public QueryBuilder<T> desc(String... names) {
		descend(names).orderDescending();
		return this;
	}

	public ObjectSet<T> execute(String... names) {
		long time = System.currentTimeMillis();
		ObjectSet<T> set = descend(names).execute();
		if (performance.isDebugEnabled()) {
			time = System.currentTimeMillis() - time;
			StackTraceElement[] elems = Thread.currentThread().getStackTrace();
			if (elems.length > 2) {
				performance.debug(new StringBuilder("[Query] ")
						.append(elems[2].getClassName()).append('#')
						.append(elems[2].getMethodName()).append('@')
						.append(elems[2].getLineNumber()).append("=")
						.append(set.size()).append(" : ").append(time)
						.append("ms").toString());
			}
		}
		return set;
	}

	/**
	 * @return the query
	 */
	public Query getQuery() {
		return query;
	}

}
