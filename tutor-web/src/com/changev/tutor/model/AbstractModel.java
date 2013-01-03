/*
 * File   AbstractModel.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;

/**
 * <p>
 * 定义公共模型属性。
 * </p>
 * 
 * @author ren
 * 
 */
public abstract class AbstractModel implements Serializable, Cloneable {

	private static final long serialVersionUID = 4390399593602862270L;

	private Date createDateTime;
	private Date updateDateTime;
	private Boolean deleted;

	public AbstractModel() {
	}

	public AbstractModel(AbstractModel copy) {
		this.setCreateDateTime(copy.getCreateDateTime());
		this.setUpdateDateTime(copy.getUpdateDateTime());
		this.setDeleted(copy.getDeleted());
	}

	@Override
	public AbstractModel clone() {
		try {
			return (AbstractModel) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	@Override
	public String toString() {
		try {
			StringBuilder buf = new StringBuilder(getClass().getSimpleName());
			buf.append(" {");
			for (Class<?> cls = getClass(); cls != Object.class; cls = cls
					.getSuperclass()) {
				Field[] flds = cls.getDeclaredFields();
				boolean comma = false;
				for (int i = 0; i < flds.length; i++) {
					if (Modifier.isStatic(flds[i].getModifiers()))
						continue;
					if (comma)
						buf.append(", ");
					comma = true;
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
	 * @return the createDateTime
	 */
	public Date getCreateDateTime() {
		return createDateTime;
	}

	/**
	 * @param createDateTime
	 *            the createDateTime to set
	 */
	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}

	/**
	 * @return the updateDateTime
	 */
	public Date getUpdateDateTime() {
		return updateDateTime;
	}

	/**
	 * @param updateDateTime
	 *            the updateDateTime to set
	 */
	public void setUpdateDateTime(Date updateDateTime) {
		this.updateDateTime = updateDateTime;
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
