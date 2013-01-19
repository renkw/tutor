/*
 * File   ParamValidator.java
 * Create 2013/01/14
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.changev.tutor.util.TextPattern;
import com.changev.tutor.web.Messages;

/**
 * <p>
 * 验证表单输入。
 * </p>
 * 
 * @author ren
 * 
 */
public abstract class ParamValidator {

	private static Map<String, Class<?>> validatorTypes = Collections
			.synchronizedMap(new HashMap<String, Class<?>>());

	/**
	 * <p>
	 * 取得设定的验证器类型。
	 * </p>
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Class<ParamValidator> getValidatorType(String name) {
		return (Class<ParamValidator>) validatorTypes.get(name);
	}

	/**
	 * <p>
	 * 生成指定的验证器实例。
	 * </p>
	 * 
	 * @param name
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static ParamValidator getValidator(String name) {
		try {
			return getValidatorType(name).newInstance();
		} catch (InstantiationException e) {
			throw new UnsupportedOperationException(e);
		} catch (IllegalAccessException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	/**
	 * <p>
	 * 设置设定的验证器类型。
	 * </p>
	 * 
	 * @param name
	 * @param type
	 */
	public static void setValidatorType(String name,
			Class<? extends ParamValidator> type) {
		validatorTypes.put(name, type);
	}

	static {
		InputStream stream = ParamValidator.class
				.getResourceAsStream("validators.properties");
		if (stream != null) {
			Properties props = new Properties();
			try {
				props.load(stream);
				for (Enumeration<?> en = props.keys(); en.hasMoreElements();) {
					String key = (String) en.nextElement();
					validatorTypes.put(key,
							Class.forName(props.getProperty(key)));
				}
			} catch (ClassNotFoundException e) {
				throw new ExceptionInInitializerError(e);
			} catch (IOException e) {
				throw new ExceptionInInitializerError(e);
			} finally {
				IOUtils.closeQuietly(stream);
			}
		}
	}

	protected boolean multiple;
	protected String messageName;
	protected TextPattern message;
	protected Map<String, Object> args = Collections.emptyMap();

	/**
	 * <p>
	 * 验证表单中输入参数是否正确。
	 * </p>
	 * 
	 * @param request
	 * @param msg
	 * @return
	 */
	public boolean validate(HttpServletRequest request, Messages msg) {
		String name = getName();
		boolean v = multiple ? validate(request.getParameterValues(name))
				: validate(request.getParameter(name));
		return addError(v, msg);
	}

	/**
	 * <p>
	 * 相同于{@link #validate(HttpServletRequest, Messages) validate(request,
	 * Messages.get(request))}。
	 * </p>
	 * 
	 * @param request
	 * @return
	 */
	public final boolean validate(HttpServletRequest request) {
		return validate(request, Messages.get(request));
	}

	/**
	 * <p>
	 * 验证单值是否正确。
	 * </p>
	 * 
	 * @param v
	 * @return
	 */
	protected boolean validate(String v) {
		return true;
	}

	/**
	 * <p>
	 * 验证多值是否正确。
	 * </p>
	 * 
	 * @param v
	 * @return
	 */
	protected boolean validate(String[] v) {
		if (v != null && v.length != 0) {
			for (int i = 0; i < v.length; i++) {
				if (!validate(v[i]))
					return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * 添加验证失败消息。
	 * </p>
	 * 
	 * @param ret
	 * @param msg
	 * @return
	 */
	protected boolean addError(boolean ret, Messages msg) {
		if (!ret && msg != null && message != null) {
			String name = StringUtils.defaultString(messageName, getName());
			msg.addError(name, message.toString(args));
		}
		return ret;
	}

	/**
	 * <p>
	 * 取得验证用的设定值。
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return args.get(key);
	}

	/**
	 * <p>
	 * 设置验证用的设定值。
	 * </p>
	 * 
	 * @param key
	 * @param value
	 */
	public void set(String key, Object value) {
		if (args == null || args == Collections.EMPTY_MAP)
			args = new HashMap<String, Object>();
		args.put(key, value);
	}

	/**
	 * @return 参数名称
	 */
	public String getName() {
		return (String) get("name");
	}

	/**
	 * @param name
	 *            参数名称
	 */
	public void setName(String name) {
		set("name", name);
	}

	/**
	 * @return the args
	 */
	public Map<String, Object> getArgs() {
		return args;
	}

	/**
	 * @param args
	 *            the args to set
	 */
	public void setArgs(Map<String, Object> args) {
		this.args = args;
	}

	/**
	 * @return the multiple
	 */
	public boolean isMultiple() {
		return multiple;
	}

	/**
	 * @param multiple
	 *            the multiple to set
	 */
	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	/**
	 * @return the messageName
	 */
	public String getMessageName() {
		return messageName;
	}

	/**
	 * @param messageName
	 *            the messageName to set
	 */
	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message == null ? null : message.toString();
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = TextPattern.parse(message);
	}

}
