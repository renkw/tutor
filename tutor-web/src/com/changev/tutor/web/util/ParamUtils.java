/*
 * File   ParamUtils.java
 * Create 2013/01/28
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.util;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * 变换请求参数。
 * </p>
 * 
 * @author ren
 * 
 */
public final class ParamUtils {

	/**
	 * <p>
	 * 如果参数为空，返回Null。
	 * </p>
	 * 
	 * @param s
	 * @return
	 */
	public static String emptyNull(String s) {
		return StringUtils.isEmpty(s) ? null : s;
	}

	/**
	 * <p>
	 * 如果参数为空，返回Null。
	 * </p>
	 * 
	 * @param s
	 * @return
	 */
	public static String emptyNull(String[] s, int index) {
		return s == null || index >= s.length ? null : emptyNull(s[index]);
	}

	/**
	 * <p>
	 * 如果参数为空，返回Null。
	 * </p>
	 * 
	 * @param s
	 * @return
	 */
	public static Boolean boolNull(String s) {
		return StringUtils.isEmpty(s) ? null : Boolean.valueOf(s);
	}

	/**
	 * <p>
	 * 如果参数为空，返回Null。
	 * </p>
	 * 
	 * @param s
	 * @return
	 */
	public static Boolean boolNull(String[] s, int index) {
		return s == null || index >= s.length ? null : boolNull(s[index]);
	}

	/**
	 * <p>
	 * 如果参数为空，返回Null。
	 * </p>
	 * 
	 * @param s
	 * @return
	 */
	public static Byte byteNull(String s) {
		return StringUtils.isEmpty(s) ? null : Byte.valueOf(s);
	}

	/**
	 * <p>
	 * 如果参数为空，返回Null。
	 * </p>
	 * 
	 * @param s
	 * @return
	 */
	public static Byte byteNull(String[] s, int index) {
		return s == null || index >= s.length ? null : byteNull(s[index]);
	}

	/**
	 * <p>
	 * 如果参数为空，返回Null。
	 * </p>
	 * 
	 * @param s
	 * @return
	 */
	public static Short shortNull(String s) {
		return StringUtils.isEmpty(s) ? null : Short.valueOf(s);
	}

	/**
	 * <p>
	 * 如果参数为空，返回Null。
	 * </p>
	 * 
	 * @param s
	 * @return
	 */
	public static Short shortNull(String[] s, int index) {
		return s == null || index >= s.length ? null : shortNull(s[index]);
	}

	/**
	 * <p>
	 * 如果参数为空，返回Null。
	 * </p>
	 * 
	 * @param s
	 * @return
	 */
	public static Integer intNull(String s) {
		return StringUtils.isEmpty(s) ? null : Integer.valueOf(s);
	}

	/**
	 * <p>
	 * 如果参数为空，返回Null。
	 * </p>
	 * 
	 * @param s
	 * @return
	 */
	public static Integer intNull(String[] s, int index) {
		return s == null || index >= s.length ? null : intNull(s[index]);
	}

	/**
	 * <p>
	 * 如果参数为空，返回Null。
	 * </p>
	 * 
	 * @param s
	 * @return
	 */
	public static Long longNull(String s) {
		return StringUtils.isEmpty(s) ? null : Long.valueOf(s);
	}

	/**
	 * <p>
	 * 如果参数为空，返回Null。
	 * </p>
	 * 
	 * @param s
	 * @return
	 */
	public static Long longNull(String[] s, int index) {
		return s == null || index >= s.length ? null : longNull(s[index]);
	}

	/**
	 * <p>
	 * 如果参数为空，返回Null。
	 * </p>
	 * 
	 * @param s
	 * @return
	 */
	public static Float floatNull(String s) {
		return StringUtils.isEmpty(s) ? null : Float.valueOf(s);
	}

	/**
	 * <p>
	 * 如果参数为空，返回Null。
	 * </p>
	 * 
	 * @param s
	 * @return
	 */
	public static Float floatNull(String[] s, int index) {
		return s == null || index >= s.length ? null : floatNull(s[index]);
	}

	/**
	 * <p>
	 * 如果参数为空，返回Null。
	 * </p>
	 * 
	 * @param s
	 * @return
	 */
	public static Double doubleNull(String s) {
		return StringUtils.isEmpty(s) ? null : Double.valueOf(s);
	}

	/**
	 * <p>
	 * 如果参数为空，返回Null。
	 * </p>
	 * 
	 * @param s
	 * @return
	 */
	public static Double doubleNull(String[] s, int index) {
		return s == null || index >= s.length ? null : doubleNull(s[index]);
	}

	/**
	 * <p>
	 * 如果参数为空，返回Null。
	 * </p>
	 * 
	 * @param enumType
	 * @param s
	 * @return
	 */
	public static <T extends Enum<T>> T enumNull(Class<T> enumType, String s) {
		return StringUtils.isEmpty(s) ? null : Enum.valueOf(enumType, s);
	}

	/**
	 * <p>
	 * 如果参数为空，返回Null。
	 * </p>
	 * 
	 * @param enumType
	 * @param s
	 * @return
	 */
	public static <T extends Enum<T>> T enumNull(Class<T> enumType, String[] s,
			int index) {
		return s == null || index >= s.length ? null : enumNull(enumType,
				s[index]);
	}

}
