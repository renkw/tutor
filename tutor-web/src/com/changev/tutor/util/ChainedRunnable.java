/*
 * File   ChainedRunnable.java
 * Create 2012/12/27
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.util;

/**
 * <p>
 * Runnable对象链。
 * </p>
 * 
 * @author ren
 * 
 */
public class ChainedRunnable implements Runnable {

	private Runnable next;

	@Override
	public void run() {
		if (next != null)
			next.run();
	}

	/**
	 * @return the next
	 */
	public Runnable getNext() {
		return next;
	}

	/**
	 * @param next
	 *            the next to set
	 */
	public void setNext(Runnable next) {
		this.next = next;
	}

}
