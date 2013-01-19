/*
 * File   TextPattern.java
 * Create 2013/01/14
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;

/**
 * <p>
 * 与MessageFormat类似，替换字符串中指定的变量。
 * </p>
 * 
 * <p>
 * Pattern的变量格式：<br>
 * {<i>变量名[:变量格式]</i>}<br>
 * 变量名符合标准Java命名规则。<br>
 * 变量格式符合Formatter定义的格式。<br>
 * 用{{}输出大括号。
 * </p>
 * 
 * @author ren
 * @see MessageFormat
 * @see Formatter
 */
public class TextPattern implements Cloneable {

	public static final TextPattern EMPTY = new TextPattern(new String[0]);

	String[] pieces;

	public static TextPattern parse(String s) {
		if (s == null || s.isEmpty())
			return EMPTY;

		String[] pieces;
		int rep1 = s.indexOf('{'), rep2 = 0;
		if (rep1 == -1) {
			pieces = new String[] { s };
		} else {
			List<String> list = new ArrayList<String>();
			do {
				int t = s.indexOf('}', rep1 + 1);
				if (t == -1)
					break;
				list.add(s.substring(rep2, rep1));
				list.add(s.substring(rep1 + 1, t).trim());
				rep2 = t + 1;
			} while ((rep1 = s.indexOf('{', rep2)) != -1);
			list.add(s.substring(rep2));
			pieces = list.toArray(new String[list.size()]);
		}
		return new TextPattern(pieces);
	}

	public static String toString(String s, Map<String, Object> vars) {
		if (s == null || s.indexOf('{') == -1)
			return s;
		return parse(s).toString(vars);
	}

	public static String toString(String s, Object... vars) {
		if (s == null || s.indexOf('{') == -1)
			return s;
		return parse(s).toString(vars);
	}

	private TextPattern(String[] pieces) {
		this.pieces = pieces;
	}

	public String[] getVarNames() {
		if (pieces.length < 2)
			return EMPTY.pieces;
		String[] vars = new String[pieces.length / 2];
		for (int i = 0; i < vars.length; i++)
			vars[i] = pieces[i * 2 + 1];
		return vars;
	}

	public String toString(Map<String, ?> vars) {
		if (pieces.length == 0)
			return "";
		if (pieces.length == 1)
			return pieces[0];
		StringBuilder sb = new StringBuilder(pieces[0]);
		for (int i = 1; i < pieces.length; i += 2) {
			String s = pieces[i];
			int t = s.indexOf(':');
			if (vars != null) {
				s = t == -1 ? ObjectUtils.toString(vars.get(s))
						: String.format(s.substring(t + 1),
								vars.get(s.substring(0, t)));
				sb.append(s);
			}
			sb.append(pieces[i + 1]);
		}
		return sb.toString();
	}

	public String toString(Object... vars) {
		if (pieces.length == 0)
			return "";
		if (pieces.length == 1)
			return pieces[0];
		StringBuilder sb = new StringBuilder(pieces[0]);
		for (int i = 1; i < pieces.length; i += 2) {
			String s = pieces[i];
			int t = s.indexOf(':');
			if (vars != null) {
				int idx = t == -1 ? Integer.parseInt(s) : Integer.parseInt(s
						.substring(0, t));
				if (idx < vars.length) {
					s = t == -1 ? ObjectUtils.toString(vars[idx]) : String
							.format(s.substring(t + 1), vars[idx]);
					sb.append(s);
				}
			}
			sb.append(pieces[i + 1]);
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		if (pieces.length == 0)
			return "";
		if (pieces.length == 1)
			return pieces[0];
		StringBuilder sb = new StringBuilder(pieces[0]);
		for (int i = 1; i < pieces.length; i += 2) {
			sb.append('{').append(pieces[i]).append('}').append(pieces[i + 1]);
		}
		return sb.toString();
	}

}
