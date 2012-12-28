/*
 * File   Validator.java
 * Create 2012/12/28
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.util;

/**
 * <p>
 * 表单输入验证。
 * </p>
 * 
 * <p>
 * 定义的所有验证方法如果返回true，则表示<strong>验证失败</strong>。
 * </p>
 * 
 * @author ren
 * 
 */
public final class Validator {

	public static boolean required(String v) {
		return v == null || v.isEmpty();
	}

	public static boolean length(String v, int minLen, int maxLen) {
		return v != null && (v.length() <= minLen || v.length() >= maxLen);
	}

	public static boolean minLength(String v, int len) {
		return v != null && v.length() <= len;
	}

	public static boolean maxLength(String v, int len) {
		return v != null && v.length() >= len;
	}

	public static boolean numeric(String v) {
		if (v != null) {
			for (int i = 0; i < v.length(); i++) {
				if (v.charAt(i) < '0' || v.charAt(i) > '9')
					return true;
			}
		}
		return false;
	}

	public static boolean matches(String v, String pattern) {
		return v != null && v.matches(pattern);
	}

	public static boolean id(String v) {
		if (v != null) {
			for (int i = 0; i < v.length(); i++) {
				if (!Character.isJavaIdentifierPart(v.charAt(i)))
					return true;
			}
		}
		return false;
	}

}
