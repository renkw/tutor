/*
 * File   ParamValidators.java
 * Create 2013/01/14
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

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
	private boolean replaceArgs;

	@Override
	public boolean validate(HttpServletRequest request, Messages msg) {
		boolean ret = true;
		if (validators != null) {
			for (ParamValidator v : validators) {
				if (replaceArgs)
					v.args = args;
				if (!v.validate(request, msg)) {
					ret = false;
					if (failStop)
						break;
				}
			}
		}
		return addError(ret, msg);
	}

	@Override
	protected boolean validate(String v) {
		return false;
	}

	/**
	 * <p>
	 * 设置验证器名。
	 * </p>
	 * 
	 * <p>
	 * 用分号分隔每个<strong>验证器名:失败消息</strong>，默认replaceArgs=true,failStop=true。
	 * </p>
	 * 
	 * @param names
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public void setValidatorNames(String names) throws InstantiationException,
			IllegalAccessException {
		replaceArgs = true;
		failStop = true;
		validators = new ArrayList<ParamValidator>();
		for (String vn : StringUtils.split(names, ';')) {
			int i = (vn = vn.trim()).indexOf(':');
			ParamValidator v = getValidator(i == -1 ? vn : vn.substring(0, i));
			if (i != -1)
				v.setMessage(vn.substring(i + 1));
			validators.add(v);
		}
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
	 * @return the replaceArgs
	 */
	public boolean isReplaceArgs() {
		return replaceArgs;
	}

	/**
	 * @param replaceArgs
	 *            the replaceArgs to set
	 */
	public void setReplaceArgs(boolean replaceArgs) {
		this.replaceArgs = replaceArgs;
	}

}
