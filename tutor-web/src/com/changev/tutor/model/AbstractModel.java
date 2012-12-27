/*
 * File   AbstractModel.java
 * Create 2012/12/26
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
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
