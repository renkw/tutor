/*
 * File   NamedLock.java
 * Create 2012/12/28
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ren
 * 
 */
public class NamedLock {

	private Map<String, Object> locks = new HashMap<>();

	public Object lock(String name) {
		synchronized (locks) {
			Object lock;
			do {
				lock = locks.get(name);
				if (lock == null) {
					lock = new Object();
					locks.put(name, lock);
					break;
				}
				synchronized (lock) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			} while (true);
			return lock;
		}
	}

	public void release(String name) {
		synchronized (locks) {
			Object lock = locks.remove(name);
			if (lock != null) {
				synchronized (lock) {
					lock.notify();
				}
			}
		}
	}

}
