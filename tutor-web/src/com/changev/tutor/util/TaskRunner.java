/*
 * File   TaskRunner.java
 * Create 2012/12/27
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.util;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>
 * 执行定时器任务。
 * </p>
 * 
 * @author ren
 * 
 */
public class TaskRunner extends ChainedRunnable {

	private Timer timer;
	private TimerTask task;
	private Date firstTime;
	private long delay;
	private long period;
	private boolean fixed;

	@Override
	public void run() {
		if (task == null)
			return;
		if (timer == null)
			timer = new Timer();

		if (fixed) {
			if (firstTime != null)
				timer.scheduleAtFixedRate(task, firstTime, period);
			else
				timer.scheduleAtFixedRate(task, delay, period);
		} else {
			if (firstTime != null)
				timer.schedule(task, firstTime, period);
			else
				timer.schedule(task, delay, period);
		}

		super.run();
	}

	/**
	 * @return the timer
	 */
	public Timer getTimer() {
		return timer;
	}

	/**
	 * @param timer
	 *            the timer to set
	 */
	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	/**
	 * @return the task
	 */
	public TimerTask getTask() {
		return task;
	}

	/**
	 * @param task
	 *            the task to set
	 */
	public void setTask(TimerTask task) {
		this.task = task;
	}

	/**
	 * @return the firstTime
	 */
	public Date getFirstTime() {
		return firstTime;
	}

	/**
	 * @param firstTime
	 *            the firstTime to set
	 */
	public void setFirstTime(Date firstTime) {
		this.firstTime = firstTime;
	}

	/**
	 * @return the delay
	 */
	public long getDelay() {
		return delay;
	}

	/**
	 * @param delay
	 *            the delay to set
	 */
	public void setDelay(long delay) {
		this.delay = delay;
	}

	/**
	 * @return the period
	 */
	public long getPeriod() {
		return period;
	}

	/**
	 * @param period
	 *            the period to set
	 */
	public void setPeriod(long period) {
		this.period = period;
	}

	/**
	 * @return the fixed
	 */
	public boolean isFixed() {
		return fixed;
	}

	/**
	 * @param fixed
	 *            the fixed to set
	 */
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

}
