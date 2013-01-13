/*
 * File   Callback.java
 * Create 2013/01/13
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor;

/**
 * <p>
 * 定义回调接口。
 * </p>
 * 
 * @author ren
 * 
 */
public interface Callback<T> {

	Object callback(T arg) throws Throwable;
}
