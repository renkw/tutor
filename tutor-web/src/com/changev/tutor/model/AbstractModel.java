/*
 * File   AbstractModel.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

/**
 * <p>
 * 定义公共模型属性。
 * </p>
 * 
 * @author ren
 * 
 */
public abstract class AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Timestamp createDate;
	private Timestamp updateDate;
	private Boolean deleted;

	public AbstractModel() {
	}

	public AbstractModel(AbstractModel copy) {
		this.setCreateDate(copy.getCreateDate());
		this.setUpdateDate(copy.getUpdateDate());
		this.setDeleted(copy.getDeleted());
	}

	@Override
	public String toString() {
		try {
			StringBuilder buf = new StringBuilder(getClass().getSimpleName());
			buf.append(" {");
			for (Class<?> cls = getClass(); cls != Object.class; cls = cls
					.getSuperclass()) {
				Field[] flds = cls.getDeclaredFields();
				for (int i = 0; i < flds.length; i++) {
					if (i != 0)
						buf.append(", ");
					flds[i].setAccessible(true);
					buf.append(flds[i].getName()).append(" = ")
							.append(flds[i].get(this));
				}
			}
			buf.append("}");
			return buf.toString();
		} catch (IllegalAccessException e) {
			return super.toString();
		}
	}

	/**
	 * @return the createDate
	 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate
	 *            the createDate to set
	 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the updateDate
	 */
	public Timestamp getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate
	 *            the updateDate to set
	 */
	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the deleted
	 */
	public Boolean getDeleted() {
		return deleted;
	}

	/**
	 * @param deleted
	 *            the deleted to set
	 */
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

}
