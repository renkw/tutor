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

	/** 在HttpSession中的SessionContainer实例键 */
	public static final String KEY_SESSION_CONTAINER = "com.changev.tutor.KEY_SESSION_CONTAINER";

	/** 默认的Log4j配置文件路径 */
	public static final String DEFAULT_LOG4J_CONFIG_PATH = "//META-INF/com.changev.tutor.log4j.xml";

	/** 默认的Spring配置文件路径 */
	public static final String DEFAULT_BEAN_CONFIG_PATH = "//META-INF/com.changev.tutor.beans.xml";

	/** 默认的上传文件目录 */
	public static final String DEFAULT_UPLOAD_PATH = "//../upload/";

	/** 默认的db4o数据文件 */
	public static final String DEFAULT_DATAFILE_PATH = "//../data/runtime";

	/** 默认的日期格式 */
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

	/** 默认的日期时间格式 */
	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/** 默认的货币格式 */
	public static final String DEFAULT_CURRENCY_FORMAT = "#,##0.00";

	static String contextRootPath = "";

	/**
	 * @param path
	 *            the contextRootPath to set
	 */
	public static void setContextRootPath(String path) {
		contextRootPath = StringUtils.defaultString(path);
	}

	/**
	 * @return the contextRootPath
	 */
	public static String getContextRootPath() {
		return contextRootPath;
	}

	/**
	 * <p>
	 * 取得指定路径对于的真实文件系统路径。
	 * </p>
	 * 
	 * <p>
	 * 如果指定路径以//前缀，则视为相对于网站上下文路径。
	 * </p>
	 * 
	 * @param path
	 * @return
	 */
	public static String getRealPath(String path) {
		return path.startsWith("//") ? (contextRootPath + path.substring(1))
				: path;
	}

	/**
	 * <p>
	 * 格式化日期对象。
	 * </p>
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		return new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * <p>
	 * 解析日期表示字符串。
	 * </p>
	 * 
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static Date parseDate(String str, String pattern) {
		try {
			return new SimpleDateFormat(pattern).parse(str);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * <p>
	 * 格式化日期对象（仅日期部分）。
	 * </p>
	 * 
	 * <p>
	 * 相同于formatDate(date, DEFAULT_DATE_FORMAT)。
	 * </p>
	 * 
	 * @param date
	 * @return
	 * @see #formatDate(Date, String)
	 */
	public static String formatDate(Date date) {
		return formatDate(date, DEFAULT_DATE_FORMAT);
	}

	/**
	 * <p>
	 * 解析日期表示字符串（仅日期部分）。
	 * </p>
	 * 
	 * <p>
	 * 相同于parseDate(str, DEFAULT_DATE_FORMAT)。
	 * </p>
	 * 
	 * @param str
	 * @return
	 */
	public static Date parseDate(String str) {
		return parseDate(str, DEFAULT_DATE_FORMAT);
	}

	/**
	 * <p>
	 * 格式化日期对象（日期和时间部分）。
	 * </p>
	 * 
	 * <p>
	 * 相同于formatDate(date, DEFAULT_DATETIME_FORMAT)。
	 * </p>
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, DEFAULT_DATETIME_FORMAT);
	}

	/**
	 * <p>
	 * 解析日期表示字符串（日期和时间部分）。
	 * </p>
	 * 
	 * <p>
	 * 相同于parseDate(str, DEFAULT_DATETIME_FORMAT)。
	 * </p>
	 * 
	 * @param str
	 * @return
	 */
	public static Date parseDateTime(String str) {
		return parseDate(str, DEFAULT_DATETIME_FORMAT);
	}

	/**
	 * <p>
	 * 格式化数字。
	 * </p>
	 * 
	 * @param num
	 * @param pattern
	 * @return
	 */
	public static String formatNumber(Number num, String pattern) {
		return new DecimalFormat(pattern).format(num);
	}

	/**
	 * <p>
	 * 解析数字字符串。
	 * </p>
	 * 
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static Number parseNumber(String str, String pattern) {
		try {
			return new DecimalFormat(pattern).parse(str);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * <p>
	 * 格式化货币。
	 * </p>
	 * 
	 * <p>
	 * 相同于formatNumber(num, DEFAULT_CURRENCY_FORMAT)。
	 * </p>
	 * 
	 * @param num
	 * @return
	 */
	public static String formatCurrency(Number num) {
		return formatNumber(num, DEFAULT_CURRENCY_FORMAT);
	}

	/**
	 * <p>
	 * 解析数字字符串。
	 * </p>
	 * 
	 * <p>
	 * 相同于parseNumber(str, DEFAULT_CURRENCY_FORMAT)。
	 * </p>
	 * 
	 * @param str
	 * @return
	 */
	public static Number parseCurrency(String str) {
		return parseNumber(str, DEFAULT_CURRENCY_FORMAT);
	}

}
