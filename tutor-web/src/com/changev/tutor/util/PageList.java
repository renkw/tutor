/*
 * File   PageList.java
 * Create 2013/01/31
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.changev.tutor.PerformanceIgnore;
import com.changev.tutor.Tutor;
import com.db4o.ObjectSet;
import com.db4o.ext.ExtObjectContainer;
import com.db4o.query.Predicate;
import com.db4o.query.QueryComparator;

/**
 * <p>
 * 数据集分页。
 * </p>
 * 
 * <p>
 * 每次从当前线程ObjectContainer中取得最新数据。
 * </p>
 * 
 * @author ren
 * @see Tutor#getCurrentContainer()
 */
public class PageList<T> implements Serializable, PerformanceIgnore {

	private static final long serialVersionUID = -6174504713384843715L;

	private long[] idList;
	private int pageItems = 10;
	private int totalPages;

	public PageList(ObjectSet<T> set) {
		idList = set.ext().getIDs();
	}

	public PageList(Predicate<T> predicate) {
		this(Tutor.getCurrentContainer().query(predicate));
	}

	public PageList(Predicate<T> predicate, QueryComparator<T> comparator) {
		this(Tutor.getCurrentContainer().query(predicate, comparator));
	}

	public PageList(List<T> list) {
		ExtObjectContainer objc = Tutor.getCurrentContainerExt();
		idList = new long[list.size()];
		for (int i = 0; i < idList.length; i++)
			idList[i] = objc.getID(list.get(i));
	}

	/**
	 * <p>
	 * 取得指定页的数据列表。
	 * </p>
	 * 
	 * @param page
	 *            页码，范围从1开始到{@link #getTotalPages() totalPages}
	 * @param asc
	 *            按原始查询结果集的正序或倒序取
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> getPage(int page, boolean asc) {
		ExtObjectContainer objc = Tutor.getCurrentContainerExt();
		List<T> list = new ArrayList<T>(pageItems);
		if (asc) {
			int index = (page - 1) * pageItems;
			for (int i = 0; i < pageItems && index < idList.length; i++) {
				list.add((T) objc.getByID(idList[index++]));
			}
		} else {
			int index = idList.length - (page - 1) * pageItems;
			for (int i = 0; i < pageItems && index > 0; i++) {
				list.add((T) objc.getByID(idList[--index]));
			}
		}
		return list;
	}

	/**
	 * <p>
	 * 相同于{@link #getPage(int, boolean) getPage(page, true)}。
	 * </p>
	 * 
	 * @param page
	 * @return
	 */
	public List<T> getPage(int page) {
		return getPage(page, true);
	}

	/**
	 * 
	 * @return 数据总数
	 */
	public int getTotalItems() {
		return idList.length;
	}

	/**
	 * 
	 * @return 分页总数
	 */
	public int getTotalPages() {
		return totalPages;
	}

	/**
	 * @return the pageItems
	 */
	public int getPageItems() {
		return pageItems;
	}

	/**
	 * @param pageItems
	 *            the pageItems to set
	 */
	public void setPageItems(int pageItems) {
		this.pageItems = pageItems;
		this.totalPages = (idList.length + pageItems - 1) / pageItems;
	}

}
