/*
 * File   ObjectContainerWrapper.java
 * Create 2013/01/14
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor;

import java.io.Serializable;
import java.util.Comparator;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;
import com.db4o.ext.ExtObjectContainer;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import com.db4o.query.QueryComparator;

/**
 * <p>
 * db4o的ObjectContainer包裹器。
 * </p>
 * 
 * <p>
 * 原始的ObjectContainer实例是懒加载方式，保证每次使用时一定有可用的连接。<br>
 * 关闭该包裹器只是调用原始的ObjectContainer.purge()，并未真正关闭连接。<br>
 * 当包裹器被回收时，自动关闭原始的ObjectContainer实例。
 * </p>
 * 
 * @author ren
 * @see ObjectContainer
 */
public class ObjectContainerWrapper implements ObjectContainer, Serializable,
		PerformanceIgnore {

	private static final long serialVersionUID = -6221167529547830146L;

	private boolean closeImmediately;
	protected transient ObjectContainer original;

	public ObjectContainerWrapper() {
	}

	public ObjectContainerWrapper(boolean closeImmediately) {
		this.closeImmediately = closeImmediately;
	}

	protected ObjectContainer original() {
		if (original == null || original.ext().isClosed())
			original = Tutor.getRootContainer().openSession();
		return original;
	}

	@Override
	protected void finalize() throws Throwable {
		if (original != null && !original.ext().isClosed())
			original.close();
		original = null;
	}

	@Override
	public void activate(Object obj, int depth) throws Db4oIOException,
			DatabaseClosedException {
		original().activate(obj, depth);
	}

	@Override
	public boolean close() throws Db4oIOException {
		if (original != null && closeImmediately) {
			original.close();
			original = null;
		}
		// always return true
		return true;
	}

	@Override
	public void commit() throws Db4oIOException, DatabaseClosedException,
			DatabaseReadOnlyException {
		if (original != null && !original.ext().isClosed())
			original.commit();
	}

	@Override
	public void deactivate(Object obj, int depth)
			throws DatabaseClosedException {
		original().deactivate(obj, depth);
	}

	@Override
	public void delete(Object obj) throws Db4oIOException,
			DatabaseClosedException, DatabaseReadOnlyException {
		original().delete(obj);
	}

	@Override
	public ExtObjectContainer ext() {
		return original().ext();
	}

	@Override
	public <T> ObjectSet<T> queryByExample(Object template)
			throws Db4oIOException, DatabaseClosedException {
		long time = System.currentTimeMillis();
		ObjectSet<T> set = original().queryByExample(template);
		if (Tutor.isPerformanceEnabled())
			Tutor.performance("Example", System.currentTimeMillis() - time,
					set.size());
		return set;
	}

	@Override
	public Query query() throws DatabaseClosedException {
		return original().query();
	}

	@Override
	public <TargetType> ObjectSet<TargetType> query(Class<TargetType> clazz)
			throws Db4oIOException, DatabaseClosedException {
		long time = System.currentTimeMillis();
		ObjectSet<TargetType> set = original().query(clazz);
		if (Tutor.isPerformanceEnabled())
			Tutor.performance("Class", System.currentTimeMillis() - time,
					set.size());
		return set;
	}

	@Override
	public <TargetType> ObjectSet<TargetType> query(
			Predicate<TargetType> predicate) throws Db4oIOException,
			DatabaseClosedException {
		long time = System.currentTimeMillis();
		ObjectSet<TargetType> set = original().query(predicate);
		if (Tutor.isPerformanceEnabled())
			Tutor.performance("NativeQuery", System.currentTimeMillis() - time,
					set.size());
		return set;
	}

	@Override
	public <TargetType> ObjectSet<TargetType> query(
			Predicate<TargetType> predicate,
			QueryComparator<TargetType> comparator) throws Db4oIOException,
			DatabaseClosedException {
		long time = System.currentTimeMillis();
		ObjectSet<TargetType> set = original().query(predicate, comparator);
		if (Tutor.isPerformanceEnabled())
			Tutor.performance("NativeQuerySort", System.currentTimeMillis()
					- time, set.size());
		return set;
	}

	@Override
	public <TargetType> ObjectSet<TargetType> query(
			Predicate<TargetType> predicate, Comparator<TargetType> comparator)
			throws Db4oIOException, DatabaseClosedException {
		long time = System.currentTimeMillis();
		ObjectSet<TargetType> set = original().query(predicate, comparator);
		if (Tutor.isPerformanceEnabled())
			Tutor.performance("NativeQuerySort", System.currentTimeMillis()
					- time, set.size());
		return set;
	}

	@Override
	public void rollback() throws Db4oIOException, DatabaseClosedException,
			DatabaseReadOnlyException {
		if (original != null && !original.ext().isClosed())
			original.rollback();
	}

	@Override
	public void store(Object obj) throws DatabaseClosedException,
			DatabaseReadOnlyException {
		original().store(obj);
	}

}
