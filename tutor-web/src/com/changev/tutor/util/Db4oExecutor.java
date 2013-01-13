/*
 * File   Db4oExecutor.java
 * Create 2013/01/13
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.util;

import com.changev.tutor.Callback;
import com.changev.tutor.Tutor;
import com.db4o.ObjectContainer;

/**
 * <p>
 * 利用{@link Callback Callback}执行db4o任务。
 * </p>
 * 
 * @author ren
 * @see Callback
 * 
 */
public final class Db4oExecutor {

	/**
	 * <p>
	 * 执行读数据操作。此操作不提交事务。
	 * </p>
	 * 
	 * <p>
	 * <strong>注意!</strong><br>
	 * 不要直接修改从ObjectContainer中取得的数据。 如果需要修改，请使用克隆后的数据以保证完整性。
	 * </p>
	 * 
	 * @param c
	 * @throws Throwable
	 */
	@SuppressWarnings("unchecked")
	public static <T> T query(Callback<ObjectContainer> c) throws Throwable {
		ObjectContainer objc = Tutor.getRootContainer();
		return (T) c.callback(objc);
	}

	/**
	 * <p>
	 * {@link #query(Callback) query}的无异常版本。
	 * </p>
	 * 
	 * @param c
	 * @return
	 */
	public static <T> T querySafe(Callback<ObjectContainer> c) {
		try {
			return query(c);
		} catch (Error e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * <p>
	 * 执行写数据操作。此操作在无异常抛出时提交事务；如出现异常则回滚。
	 * </p>
	 * 
	 * @param c
	 * @throws Throwable
	 */
	@SuppressWarnings("unchecked")
	public static <T> T execute(Callback<ObjectContainer> c) throws Throwable {
		ObjectContainer objc = Tutor.getRootContainer().openSession();
		try {
			Object result = c.callback(objc);
			objc.commit();
			return (T) result;
		} catch (Throwable t) {
			objc.rollback();
			throw t;
		} finally {
			objc.close();
		}
	}

	/**
	 * <p>
	 * {@link #execute(Callback) execute}的无异常版本。
	 * </p>
	 * 
	 * @param c
	 * @return
	 */
	public static <T> T executeSafe(Callback<ObjectContainer> c) {
		try {
			return execute(c);
		} catch (Error e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

}
