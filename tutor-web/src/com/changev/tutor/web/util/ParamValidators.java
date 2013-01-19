/*
 * File   ParamValidators.java
 * Create 2013/01/14
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.changev.tutor.web.Messages;

/**
 * <p>
 * 合并的验证器。
 * </p>
 * 
 * @author ren
 * 
 */
public class ParamValidators extends ParamValidator {

	private List<ParamValidator> validators;
	private boolean failStop;
	private boolean failAll;

	@Override
	public boolean validate(HttpServletRequest request, Messages msg) {
		if (validators == null || validators.isEmpty())
			return true;

		boolean ret = failAll ? false : true;
		for (ParamValidator v : validators) {
			if (v.validate(request, msg)) {
				ret = failAll ? true : ret && true;
			} else {
				ret = failAll ? ret || false : false;
				if (failStop)
					break;
			}
		}
		return addError(ret, msg);
	}

	/**
	 * @return the validators
	 */
	public List<ParamValidator> getValidators() {
		return validators;
	}

	/**
	 * @param validators
	 *            the validators to set
	 */
	public void setValidators(List<ParamValidator> validators) {
		this.validators = validators;
	}

	/**
	 * @return the failStop
	 */
	public boolean isFailStop() {
		return failStop;
	}

	/**
	 * @param failStop
	 *            the failStop to set
	 */
	public void setFailStop(boolean failStop) {
		this.failStop = failStop;
	}

	/**
	 * @return the failAll
	 */
	public boolean isFailAll() {
		return failAll;
	}

	/**
	 * @param failAll
	 *            the failAll to set
	 */
	public void setFailAll(boolean failAll) {
		this.failAll = failAll;
	}

}
