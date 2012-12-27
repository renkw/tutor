/*
 * File  
 */
package com.changev.tutor;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 公共常量和方法定义。
 * </p>
 * 
 * @author ren
 * 
 */
public final class Tutor {

	/** 在ServletContext中的BeanFactory实例键 */
	public static final String KEY_BEAN_FACTORY = "com.changev.tutor.KEY_BEAN_FACTORY";

	/** 在HttpServletRequest中的View实例键 */
	public static final String KEY_REQUEST_VIEW = "com.changev.tutor.KEY_REQUEST_VIEW";

	public static final String KEY_SESSION_CONTAINER = "com.changev.tutor.KEY_SESSION_CONTAINER";

	/** 默认的Log4j配置文件路径 */
	public static final String DEFAULT_LOG4J_CONFIG_PATH = "//META-INF/com.changev.tutor.log4j.xml";

	/** 默认的Spring配置文件路径 */
	public static final String DEFAULT_BEAN_CONFIG_PATH = "//META-INF/com.changev.tutor.beans.xml";

	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String DEFAULT_CURRENCY_FORMAT = "#,##0.00";

	static String contextRootPath = "";

	public static void setContextRootPath(String path) {
		contextRootPath = StringUtils.defaultString(path);
	}

	public static String getContextRootPath() {
		return contextRootPath;
	}

	public static String getRealPath(String path) {
		return path.startsWith("//") ? (contextRootPath + path.substring(1))
				: path;
	}

	public static String formatDate(Date date, String pattern) {
		return new SimpleDateFormat(pattern).format(date);
	}

	public static Date parseDate(String str, String pattern) {
		try {
			return new SimpleDateFormat(pattern).parse(str);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String formatDate(Date date) {
		return formatDate(date, DEFAULT_DATE_FORMAT);
	}

	public static Date parseDate(String str) {
		return parseDate(str, DEFAULT_DATE_FORMAT);
	}

	public static String formatDateTime(Date date) {
		return formatDate(date, DEFAULT_DATETIME_FORMAT);
	}

	public static Date parseDateTime(String str) {
		return parseDate(str, DEFAULT_DATETIME_FORMAT);
	}

	public static String formatNumber(Number num, String pattern) {
		return new DecimalFormat(pattern).format(num);
	}

	public static Number parseNumber(String str, String pattern) {
		try {
			return new DecimalFormat(pattern).parse(str);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String formatCurrency(Number num) {
		return formatNumber(num, DEFAULT_CURRENCY_FORMAT);
	}

	public static Number parseCurrency(String str) {
		return parseNumber(str, DEFAULT_CURRENCY_FORMAT);
	}

}
