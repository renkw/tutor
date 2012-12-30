/*
 * File   TestService.java
 * Create 2012/12/30
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.service;

import org.apache.commons.lang.StringUtils;

import com.changev.tutor.model.UserModel;
import com.changev.tutor.web.Service;

/**
 * <p>
 * 测试服务。
 * </p>
 * 
 * @author ren
 * 
 */
public class TestService implements Service<TestService.Input> {

	@Override
	public Object run(UserModel user, Input input) throws Throwable {
		Output output = new Output();
		if (StringUtils.isEmpty(input.getCommand())) {
			output.setResult("Service is running.");
		} else {
			switch (input.getCommand()) {
			case "version":
				output.setResult("1.0");
				break;
			}
		}
		return output;
	}

	public static class Input {

		private String command;

		/**
		 * @return the command
		 */
		public String getCommand() {
			return command;
		}

		/**
		 * @param command
		 *            the command to set
		 */
		public void setCommand(String command) {
			this.command = command;
		}

	}

	public static class Output {

		private String result;

		/**
		 * @return the result
		 */
		public String getResult() {
			return result;
		}

		/**
		 * @param result
		 *            the result to set
		 */
		public void setResult(String result) {
			this.result = result;
		}

	}

}
