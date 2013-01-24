/*
 * File   AbstractModel.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;

import com.changev.tutor.Tutor;
import com.db4o.ObjectContainer;
import com.db4o.activation.ActivationPurpose;
import com.db4o.activation.Activator;
import com.db4o.config.annotations.Indexed;
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

	private Boolean deleted;
	@Indexed
	private long createTimestamp;
	private long updateTimestamp;

	private transient Activator activator;
	private transient Timestamp createDateTime;
	private transient Timestamp updateDateTime;

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
		long timesatmp = Tutor.timestamp();
		setCreateTimestamp(timesatmp);
		setUpdateTimestamp(timesatmp);
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
		setUpdateTimestamp(Tutor.timestamp());
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
	public Timestamp getCreateDateTime() {
		beforeGet();
		if (createDateTime == null && createTimestamp != 0)
			createDateTime = new Timestamp(createTimestamp);
		return createDateTime;
	}

	/**
	 * @param createDateTime
	 *            the createDateTime to set
	 */
	public void setCreateDateTime(Timestamp createDateTime) {
		beforeSet();
		this.createDateTime = createDateTime;
		this.createTimestamp = createDateTime == null ? 0 : createDateTime
				.getTime();
	}

	/**
	 * @return the updateDateTime
	 */
	public Timestamp getUpdateDateTime() {
		beforeGet();
		if (updateDateTime == null && updateTimestamp != 0)
			updateDateTime = new Timestamp(updateTimestamp);
		return updateDateTime;
	}

	/**
	 * @param updateDateTime
	 *            the updateDateTime to set
	 */
	public void setUpdateDateTime(Timestamp updateDateTime) {
		beforeSet();
		this.updateDateTime = updateDateTime;
		this.updateTimestamp = updateDateTime == null ? 0 : updateDateTime
				.getTime();
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

	/**
	 * @return the createTimestamp
	 */
	public long getCreateTimestamp() {
		beforeGet();
		return createTimestamp;
	}

	/**
	 * @param createTimestamp
	 *            the createTimestamp to set
	 */
	public void setCreateTimestamp(long createTimestamp) {
		beforeSet();
		this.createTimestamp = createTimestamp;
		this.createDateTime = null;
	}

	/**
	 * @return the updateTimestamp
	 */
	public long getUpdateTimestamp() {
		beforeGet();
		return updateTimestamp;
	}

	/**
	 * @param updateTimestamp
	 *            the updateTimestamp to set
	 */
	public void setUpdateTimestamp(long updateTimestamp) {
		beforeSet();
		this.updateTimestamp = updateTimestamp;
		this.updateDateTime = null;
	}

}
