/*
 * File  
 */
package com.changev.tutor;

import java.lang.reflect.Array;
import java.security.Key;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.RandomStringUtils;
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

	/** 在HttpRequest或HttpSession中的ObjectContainer实例键 */
	public static final String KEY_OBJECT_CONTAINER = "com.changev.tutor.KEY_OBJECT_CONTAINER";

	/** 默认的字符编码 */
	public static final String DEFAULT_ENCODING = "UTF-8";

	/** 默认的Log4j配置文件路径 */
	public static final String DEFAULT_LOG4J_CONFIG_PATH = "//META-INF/com.changev.tutor.log4j.xml";

	/** 默认的Spring配置文件路径 */
	public static final String DEFAULT_BEAN_CONFIG_PATH = "//META-INF/com.changev.tutor.beans.xml";

	/** 默认的db4o配置文件路径 */
	public static final String DEFAULT_DB4O_CONFIG_PATH = "//META-INF/com.changev.tutor.db4o-embed.properties";

	/** 默认的访问控制文件路径 */
	public static final String DEFAULT_ACL_PATH = "//META-INF/com.changev.tutor.acl.xml";

	/** 默认的上传文件目录 */
	public static final String DEFAULT_UPLOAD_PATH = "//../files/";

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
	private static ThreadLocal<ObjectContainer> currentContainer = new ThreadLocal<ObjectContainer>();

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
	 * 取得当前线程关联的ObjectContainer实例。
	 * </p>
	 * 
	 * @return
	 * @see ObjectContainerWrapper
	 */
	public static ObjectContainer getCurrentContainer() {
		return currentContainer.get();
	}

	/**
	 * <p>
	 * 取得当前线程关联的ExtObjectContainer实例。
	 * </p>
	 * 
	 * @return
	 * @see ObjectContainerWrapper
	 */
	public static ExtObjectContainer getCurrentContainerExt() {
		return getCurrentContainer().ext();
	}

	/**
	 * <p>
	 * 设置当前线程关联的ObjectContainer实例。
	 * </p>
	 * 
	 * @param container
	 */
	public static void setCurrentContainer(ObjectContainer container) {
		currentContainer.set(container);
	}

	/**
	 * <p>
	 * 提交当前事务。
	 * </p>
	 */
	public static void commitCurrent() {
		ObjectContainer objc = currentContainer.get();
		if (objc != null)
			objc.commit();
	}

	/**
	 * <p>
	 * 回滚当前事务。
	 * </p>
	 */
	public static void rollbackCurrent() {
		ObjectContainer objc = currentContainer.get();
		if (objc != null)
			objc.rollback();
	}

	/**
	 * <p>
	 * 关闭当前线程关联的ObjectContainer实例。
	 * </p>
	 * 
	 * @see ObjectContainerWrapper
	 */
	public static void closeCurrentContainer() {
		ObjectContainer objc = currentContainer.get();
		if (objc != null)
			objc.close();
	}

	/**
	 * <p>
	 * 取得对象内部ID。
	 * </p>
	 * 
	 * @param obj
	 * @return
	 */
	public static String id(Object obj) {
		if (obj == null)
			return "";
		long id = getCurrentContainerExt().getID(obj);
		return id == 0 ? "" : Long.toString(id);
	}

	/**
	 * <p>
	 * 根据对象ID取得数据。
	 * </p>
	 * 
	 * @param id
	 * @return
	 */
	public static <T> T fromId(String id) {
		if (StringUtils.isEmpty(id))
			return null;
		return getCurrentContainerExt().getByID(Long.parseLong(id));
	}

	/**
	 * <p>
	 * 从数据集中取得第一个对象，如果为空返回null。
	 * </p>
	 * 
	 * @param set
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T one(List<?> set) {
		return set.isEmpty() ? null : (T) set.get(0);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> listAsc(List<?> set, int off, int len) {
		List<T> list = new ArrayList<T>(len);
		int size = set.size();
		for (int i = 0; i < len && off < size; i++)
			list.add((T) set.get(off++));
		return list;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> listDesc(List<?> set, int off, int len) {
		List<T> list = new ArrayList<T>(len);
		for (int i = 0; i < len && off >= 0; i++)
			list.add((T) set.get(off--));
		return list;
	}

	public static <T> List<T> listAsc(List<?> set, int len) {
		return listAsc(set, 0, len);
	}

	public static <T> List<T> listDesc(List<?> set, int len) {
		return listDesc(set, set.size() - 1, len);
	}

	public static <T> List<T> listAsc(List<?> set) {
		return listAsc(set, 0, set.size());
	}

	public static <T> List<T> listDesc(List<?> set) {
		return listDesc(set, set.size() - 1, set.size());
	}

	private static volatile Map<String, Object> constants = Collections
			.emptyMap();

	/**
	 * <p>
	 * 取得系统常量。
	 * </p>
	 * 
	 * @return
	 */
	public static Map<String, Object> getConstants() {
		return constants;
	}

	/**
	 * <p>
	 * 取得系统常量。
	 * </p>
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getConstant(String name) {
		return (T) constants.get(name);
	}

	/**
	 * <p>
	 * 设置系统常量。
	 * </p>
	 * 
	 * @param c
	 */
	public static void setConstants(Map<String, Object> c) {
		if (c == null || c == Collections.EMPTY_MAP)
			constants = Collections.emptyMap();
		else
			constants = Collections.unmodifiableMap(c);
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
	 * 取得当前日历对象。
	 * </p>
	 * 
	 * @return
	 */
	public static Calendar currentCalendar() {
		return Calendar.getInstance();
	}

	/**
	 * <p>
	 * 取得当前日历对象，时间部分为00:00:00.000。
	 * </p>
	 * 
	 * @return
	 */
	public static Calendar currentDateCalendar() {
		Calendar cal = currentCalendar();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	/**
	 * <p>
	 * 取得当前日历对象，日期部分为0001/01/01。
	 * </p>
	 * 
	 * @return
	 */
	public static Calendar currentTimeCalendar() {
		Calendar cal = currentCalendar();
		cal.set(1, 0, 1);
		return cal;
	}

	/**
	 * <p>
	 * 取得当前日历对象。
	 * </p>
	 * 
	 * @return
	 */
	public static Calendar emptyCalendar() {
		Calendar cal = currentCalendar();
		cal.clear();
		return cal;
	}

	/**
	 * <p>
	 * 取得当前日期和时间对象。
	 * </p>
	 * 
	 * @return
	 */
	public static Date currentDateTime() {
		return currentCalendar().getTime();
	}

	/**
	 * <p>
	 * 取得当前日期对象，时间部分为00:00:00.000。
	 * </p>
	 * 
	 * @return
	 */
	public static Date currentDate() {
		return currentDateCalendar().getTime();
	}

	/**
	 * <p>
	 * 取得当前时间对象，日期部分为0001/01/01。
	 * </p>
	 * 
	 * @return
	 */
	public static Date currentTime() {
		return currentTimeCalendar().getTime();
	}

	/**
	 * <p>
	 * 生成日期对象。
	 * </p>
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Date date(int year, int month, int date) {
		Calendar cal = emptyCalendar();
		cal.set(year, month, date);
		return cal.getTime();
	}

	/**
	 * <p>
	 * 生成时间对象。
	 * </p>
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Date time(int hour, int minute, int second) {
		Calendar cal = emptyCalendar();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		return cal.getTime();
	}

	/**
	 * <p>
	 * 生成日期时间对象。
	 * </p>
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Date datetime(int year, int month, int date, int hour,
			int minute, int second) {
		Calendar cal = emptyCalendar();
		cal.set(year, month, date, hour, minute, second);
		return cal.getTime();
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
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		df.setLenient(true);
		return df.format(date);
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
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		df.setLenient(true);
		try {
			return df.parse(str);
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
	 * @return 当指定类型为原始类型而且对象为null，返回false。<br>
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
		return cls.isInstance(obj);
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

	/**
	 * <p>
	 * 用数组元素填充List。
	 * </p>
	 * 
	 * @param ea
	 * @return
	 */
	public static <T> List<T> toList(T... ea) {
		if (ea == null || ea.length == 0)
			return Collections.emptyList();
		List<T> list = new ArrayList<T>(ea.length);
		for (int i = 0; i < ea.length; i++)
			list.add(ea[i]);
		return list;
	}

	/**
	 * <p>
	 * 用数组元素填充Map。
	 * </p>
	 * 
	 * <p>
	 * 奇数位元素为键，偶数位元素为值。 如果数组长度为奇数，最后一个键对应的值为null。
	 * </p>
	 * 
	 * @param kv
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <K, V> Map<K, V> toMap(Object... kv) {
		if (kv == null || kv.length == 0)
			return Collections.emptyMap();
		Map map = new HashMap((kv.length + 1) / 2);
		int i = 1;
		do {
			map.put(kv[i - 1], i < kv.length ? kv[i] : null);
			i += 2;
		} while (i < kv.length);
		return map;
	}

	/**
	 * <p>
	 * 从十六制数字串转换为比特数组。
	 * </p>
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] fromHex(String s) {
		byte[] bytes = new byte[(s.length() + 1) / 2];
		for (int i = s.length() - 1; i >= 0; i -= 2) {
			int c1 = s.charAt(i), c2 = i >= 1 ? s.charAt(i - 1) : '0';
			if (c1 >= '0' && c1 <= '9')
				c1 -= '0';
			else if (c1 >= 'A' && c1 <= 'F')
				c1 -= 'A' - 10;
			else if (c1 >= 'a' && c1 <= 'f')
				c1 -= 'a' - 10;
			else
				throw new NumberFormatException(s);
			if (c2 >= '0' && c2 <= '9')
				c2 -= '0';
			else if (c2 >= 'A' && c2 <= 'F')
				c2 -= 'A' - 10;
			else if (c2 >= 'a' && c2 <= 'f')
				c2 -= 'a' - 10;
			else
				throw new NumberFormatException(s);
			bytes[i / 2] = (byte) ((c2 << 4) | (c1 & 0x0F));
		}
		return bytes;
	}

	/**
	 * <p>
	 * 从比特数组转换为十六制数字串。
	 * </p>
	 * 
	 * @param bytes
	 * @return
	 */
	public static String toHex(byte[] bytes) {
		char[] chars = new char[bytes.length * 2];
		for (int i = 0; i < bytes.length; i++) {
			int v = (bytes[i] >>> 4) & 0x0F;
			chars[i * 2] = (char) (v + (v < 10 ? '0' : 'a' - 10));
			v = bytes[i] & 0x0F;
			chars[i * 2 + 1] = (char) (v + (v < 10 ? '0' : 'a' - 10));
		}
		return new String(chars);
	}

	/**
	 * <p>
	 * 生成随机十六制数字串。
	 * </p>
	 * 
	 * @param len
	 * @return
	 */
	public static String randomHex(int len) {
		return RandomStringUtils.random(len, "0123456789abcdef");
	}

	/**
	 * <p>
	 * 取得枚举数组。
	 * </p>
	 * 
	 * <p>
	 * 用|分隔枚举名，空字符串和*表示全部。
	 * </p>
	 * 
	 * @param s
	 * @param enumType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Enum<T>> T[] enumValues(String s, Class<T> enumType) {
		if (StringUtils.isEmpty(s) || "*".equals(s))
			return enumType.getEnumConstants();
		String[] sa = StringUtils.split(s, '|');
		T[] values = (T[]) Array.newInstance(enumType, sa.length);
		for (int i = 0; i < sa.length; i++) {
			values[i] = Enum.valueOf(enumType, sa[i]);
		}
		return values;
	}

	/**
	 * <p>
	 * 把字符串的指定位置后面部分替换为*。
	 * </p>
	 * 
	 * @param s
	 * @param off
	 * @return
	 */
	public static String mask(String s, int off) {
		char[] ca = s.toCharArray();
		while (off < ca.length)
			ca[off++] = '*';
		return new String(ca);
	}

	/**
	 * <p>
	 * 把字符串的后半部分替换为*。
	 * </p>
	 * 
	 * @param s
	 * @return
	 */
	public static String mask(String s) {
		return mask(s, s.length() / 2);
	}

	/**
	 * <p>
	 * 把邮件地址的指定位置后面部分替换为*。 保留@后一个字符和域名后缀。
	 * </p>
	 * 
	 * @param s
	 * @param off
	 * @return
	 */
	public static String maskEmail(String s, int off) {
		char[] ca = s.toCharArray();
		int i1 = s.indexOf('@'), i2 = s.lastIndexOf('.');
		if (i2 == -1)
			i2 = s.length();
		if (i1 == -1)
			i1 = i2;
		while (off < i1)
			ca[off++] = '*';
		off += 2;
		while (off < i2)
			ca[off++] = '*';
		return new String(ca);
	}

	/**
	 * <p>
	 * 把邮件地址的开始2个字符后面部分替换为*。 保留@后一个字符和域名后缀。
	 * </p>
	 * 
	 * @param s
	 * @return
	 */
	public static String maskEmail(String s) {
		return maskEmail(s, 2);
	}

	private static final String[] NUM_WORD = { "零", "一", "二", "三", "四", "五",
			"六", "七", "八", "九", "十" };

	public static String numberWord(int n) {
		return NUM_WORD[n % NUM_WORD.length];
	}

}
