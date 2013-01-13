/*
 * File  
 */
package com.changev.tutor;

import java.security.Key;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;

import com.db4o.ObjectContainer;
import com.db4o.ext.ExtObjectContainer;

/**
 * <p>
 * 公共常量和方法定义。
 * </p>
 * 
 * @author ren
 * 
 */
public final class Tutor {

	/** 在HttpServletRequest中的Messages实例键 */
	public static final String KEY_MESSAGES = "com.changev.tutor.KEY_MESSAGES";

	/** 在HttpSession中的SessionContainer实例键 */
	public static final String KEY_SESSION_CONTAINER = "com.changev.tutor.KEY_SESSION_CONTAINER";

	/** 默认的字符编码 */
	public static final String DEFAULT_ENCODING = "UTF-8";

	/** 默认的Log4j配置文件路径 */
	public static final String DEFAULT_LOG4J_CONFIG_PATH = "//META-INF/com.changev.tutor.log4j.xml";

	/** 默认的Spring配置文件路径 */
	public static final String DEFAULT_BEAN_CONFIG_PATH = "//META-INF/com.changev.tutor.beans.xml";

	/** 默认的db4o配置文件路径 */
	public static final String DEFAULT_DB4O_CONFIG_PATH = "//META-INF/com.changev.tutor.db4o-embed.properties";

	/** 默认的上传文件目录 */
	public static final String DEFAULT_UPLOAD_PATH = "//../upload/";

	/** 默认的日期格式 */
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

	/** 默认的日期时间格式 */
	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/** 默认的货币格式 */
	public static final String DEFAULT_CURRENCY_FORMAT = "#,##0.00";

	/** 认证日志名 */
	public static final String AUTH_LOGGER_NAME = "com.changev.tutor.AUTH";

	/** 用户登录日志消息 */
	public static final String AUTH_LOGIN_FORMAT = "[LOGIN] {0} : {1}";

	/** 用户退出日志消息 */
	public static final String AUTH_LOGOUT_FORMAT = "[LOGOUT] {0} : {1}";

	/** 性能日志名 */
	public static final String PERFORMANCE_LOGGER_NAME = "com.changev.tutor.PERFORMANCE";

	/** 实例原型（在Freemarker中使用） */
	public static final Tutor SINGLETON = new Tutor();

	/** AES密匙 */
	public static final Key AES_KEY;

	static {
		// create random 128bit AES key
		byte[] key = new byte[16];
		new Random().nextBytes(key);
		AES_KEY = new SecretKeySpec(key, "AES");
	}

	private static String contextRootPath = "";

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

	private static BeanFactory beanFactory;

	/**
	 * @return the beanFactory
	 */
	public static BeanFactory getBeanFactory() {
		return beanFactory;
	}

	/**
	 * @param beanFactory
	 *            the beanFactory to set
	 */
	public static void setBeanFactory(BeanFactory beanFactory) {
		Tutor.beanFactory = beanFactory;
	}

	private static ExtObjectContainer rootContainer;
	private static ThreadLocal<ObjectContainer> currentContainer;

	/**
	 * @return the rootContainer
	 */
	public static ExtObjectContainer getRootContainer() {
		return rootContainer;
	}

	/**
	 * @param rootContainer
	 *            the rootContainer to set
	 */
	public static void setRootContainer(ExtObjectContainer rootContainer) {
		Tutor.rootContainer = rootContainer;
	}

	/**
	 * <p>
	 * 取得当前线程绑定的db4o会话。
	 * </p>
	 * 
	 * @return
	 */
	public static ObjectContainer getCurrentContainer() {
		ObjectContainer objc = currentContainer.get();
		if (objc == null) {
			objc = rootContainer.openSession();
			currentContainer.set(objc);
		}
		return objc;
	}

	/**
	 * <p>
	 * 关闭当前线程绑定的db4o会话。
	 * </p>
	 * 
	 * @param commit
	 *            提交或者回滚
	 */
	public static void closeCurrentContainer(boolean commit) {
		ObjectContainer objc = currentContainer.get();
		if (objc != null) {
			if (commit)
				objc.commit();
			else
				objc.rollback();
			objc.close();
			currentContainer.remove();
		}
	}

	/**
	 * <p>
	 * 取得对象内部ID。
	 * </p>
	 * 
	 * @param obj
	 * @return
	 */
	public static long id(Object obj) {
		return rootContainer.getID(obj);
	}

	/**
	 * <p>
	 * 取得当前时间戳。
	 * </p>
	 * 
	 * @return
	 */
	public static long timestamp() {
		return System.currentTimeMillis();
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
	 * 相同于{@link #formatDate(Date, String) formatDate(date,
	 * DEFAULT_DATE_FORMAT)}。
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
	 * 相同于{@link #parseDate(String, String) parseDate(str, DEFAULT_DATE_FORMAT)}
	 * 。
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
	 * 相同于{@link #formatDate(Date, String) formatDate(date,
	 * DEFAULT_DATETIME_FORMAT)}。
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
	 * 相同于{@link #parseDate(String, String) parseDate(str,
	 * DEFAULT_DATETIME_FORMAT)}。
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
	 * 相同于{@link #formatNumber(Number, String) formatNumber(num,
	 * DEFAULT_CURRENCY_FORMAT)}。
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
	 * 相同于{@link #parseNumber(String, String) parseNumber(str,
	 * DEFAULT_CURRENCY_FORMAT)}。
	 * </p>
	 * 
	 * @param str
	 * @return
	 */
	public static Number parseCurrency(String str) {
		return parseNumber(str, DEFAULT_CURRENCY_FORMAT);
	}

	/**
	 * <p>
	 * 取得对象的字符串表达形式。
	 * </p>
	 * 
	 * <p>
	 * 日期对象用{@link #DEFAULT_DATETIME_FORMAT}格式化。
	 * </p>
	 * 
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		if (obj == null)
			return "null";
		if (obj.getClass() == Date.class)
			return formatDateTime((Date) obj);
		return obj.toString();
	}

	/**
	 * <p>
	 * 测试对象是否指定类型。
	 * </p>
	 * 
	 * @param obj
	 * @param clsname
	 * @return 当指定类型为原始类型而且对象为null，返回false。<br />
	 *         当指定类型非原始类型而且对象为null，返回true。
	 * @throws ClassNotFoundException
	 */
	public static boolean is(Object obj, String clsname)
			throws ClassNotFoundException {
		Class<?> cls = ClassUtils.getClass(clsname);
		if (cls.isPrimitive()) {
			if (obj == null)
				return false;
			cls = ClassUtils.primitiveToWrapper(cls);
		}
		return obj == null || cls.isInstance(obj);
	}

	/**
	 * <p>
	 * 转换为整数。
	 * </p>
	 * 
	 * @param obj
	 * @return
	 */
	public static int parseInt(Object obj) {
		if (obj == null)
			return 0;
		if (obj instanceof Number)
			return ((Number) obj).intValue();
		if (obj instanceof Date)
			return (int) ((Date) obj).getTime();
		return Integer.parseInt(obj.toString());
	}

	/**
	 * <p>
	 * 转换为长整数。
	 * </p>
	 * 
	 * @param obj
	 * @return
	 */
	public static long parseLong(Object obj) {
		if (obj == null)
			return 0;
		if (obj instanceof Number)
			return ((Number) obj).longValue();
		if (obj instanceof Date)
			return ((Date) obj).getTime();
		return Long.parseLong(obj.toString());
	}

	/**
	 * <p>
	 * 转换为浮点数。
	 * </p>
	 * 
	 * @param obj
	 * @return
	 */
	public static double parseDouble(Object obj) {
		if (obj == null)
			return 0;
		if (obj instanceof Number)
			return ((Number) obj).doubleValue();
		if (obj instanceof Date)
			return ((Date) obj).getTime();
		return Double.parseDouble(obj.toString());
	}

}
