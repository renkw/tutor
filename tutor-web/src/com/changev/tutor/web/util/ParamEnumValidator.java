/*
 * File   ParamContainsValidator.java
 * Create 2013/01/19
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.util;


/**
 * <p>
 * 验证参数值是否符合预定值。
 * </p>
 * 
 * @author ren
 * 
 */
public class ParamEnumValidator extends ParamContainsValidator {

	private Class<? extends Enum<?>> enumType;

	/**
	 * @return the enumType
	 */
	public Class<? extends Enum<?>> getEnumType() {
		return enumType;
	}

	/**
	 * @param enumType
	 *            the enumType to set
	 */
	public void setEnumType(Class<? extends Enum<?>> enumType) {
		this.enumType = enumType;
		String[] enumNames = null;
		if (enumType != null) {
			Enum<?>[] enums = enumType.getEnumConstants();
			enumNames = new String[enums.length];
			for (int i = 0; i < enums.length; i++)
				enumNames[i] = enums[i].name();
		}
		setValues(enumNames);
	}

}
