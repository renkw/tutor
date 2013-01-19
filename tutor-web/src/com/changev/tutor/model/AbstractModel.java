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

import com.changev.tutor.Tutor;
import com.db4o.ObjectContainer;
import com.db4o.activation.ActivationPurpose;
import com.db4o.activation.Activator;
import com.db4o.ta.Activatable;

/**
 * <p>
 * 定义公共模型属性。
 * </p>
 * 
 * @author ren
 * 
 */
public abstract class AbstractModel implements Serializable, Cloneable,
		Activatable {

	private static final long serialVersionUID = 4390399593602862270L;

	private Date createDateTime;
	private Date updateDateTime;
	private Boolean deleted;

	private transient Activator activator;

	@Override
	public final void bind(Activator activator) {
		this.activator = activator;
	}

	@Override
	public final void activate(ActivationPurpose purpose) {
		if (activator != null)
			activator.activate(purpose);
	}

	/**
	 * <p>
	 * 读取可序列化对象前必须调用。
	 * </p>
	 */
	protected void beforeGet() {
		activate(ActivationPurpose.READ);
	}

	/**
	 * <p>
	 * 设置可序列化对象前必须调用。
	 * </p>
	 */
	protected void beforeSet() {
		activate(ActivationPurpose.WRITE);
	}

	/**
	 * <p>
	 * 即使简单调用，子类也必须显式覆盖该方法。
	 * </p>
	 * 
	 * @param container
	 */
	public void objectOnActivate(ObjectContainer container) {
	}

	/**
	 * <p>
	 * 即使简单调用，子类也必须显式覆盖该方法。
	 * </p>
	 * 
	 * @param container
	 */
	public void objectOnNew(ObjectContainer container) {
		Date date = Tutor.currentDateTime();
		setCreateDateTime(date);
		setUpdateDateTime(date);
		setDeleted(Boolean.FALSE);
	}

	/**
	 * <p>
	 * 即使简单调用，子类也必须显式覆盖该方法。
	 * </p>
	 * 
	 * @param container
	 */
	public void objectOnUpdate(ObjectContainer container) {
		setUpdateDateTime(Tutor.currentDateTime());
	}

	@Override
	public AbstractModel clone() {
		try {
			beforeGet();
			AbstractModel model = (AbstractModel) super.clone();
			model.activator = null;
			return model;
		} catch (CloneNotSupportedException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	@Override
	public String toString() {
		try {
			beforeGet();
			StringBuilder buf = new StringBuilder(getClass().getSimpleName());
			buf.append(" {");
			for (Class<?> cls = getClass(); cls != Object.class; cls = cls
					.getSuperclass()) {
				Field[] flds = cls.getDeclaredFields();
				boolean comma = false;
				for (int i = 0; i < flds.length; i++) {
					if (Modifier.isStatic(flds[i].getModifiers())
							|| Modifier.isTransient(flds[i].getModifiers()))
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
		beforeGet();
		return createDateTime;
	}

	/**
	 * @param createDateTime
	 *            the createDateTime to set
	 */
	public void setCreateDateTime(Date createDateTime) {
		beforeSet();
		this.createDateTime = createDateTime;
	}

	/**
	 * @return the updateDateTime
	 */
	public Date getUpdateDateTime() {
		beforeGet();
		return updateDateTime;
	}

	/**
	 * @param updateDateTime
	 *            the updateDateTime to set
	 */
	public void setUpdateDateTime(Date updateDateTime) {
		beforeSet();
		this.updateDateTime = updateDateTime;
	}

	/**
	 * @return the deleted
	 */
	public Boolean getDeleted() {
		beforeGet();
		return deleted;
	}

	/**
	 * @param deleted
	 *            the deleted to set
	 */
	public void setDeleted(Boolean deleted) {
		beforeSet();
		this.deleted = deleted;
	}

}
