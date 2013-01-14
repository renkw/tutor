/*
 * File   UserContactModel.java
 * Create 2013/01/12
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

/**
 * <p>
 * 用户联系方式。
 * </p>
 * 
 * @author ren
 * 
 */
public class UserContactModel extends AbstractModel {

	private static final long serialVersionUID = -8284576676920188592L;

	private String name;
	private String address1;
	private String address2;
	private String postcode;
	private String telephone;
	private String cellphone;
	private String fax;
	private String QQ;
	private String weibo;
	private String mailAddress;
	private Double posX;
	private Double posY;

	@Override
	public UserContactModel clone() {
		return (UserContactModel) super.clone();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		beforeGet();
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		beforeSet();
		this.name = name;
	}

	/**
	 * @return the address1
	 */
	public String getAddress1() {
		beforeGet();
		return address1;
	}

	/**
	 * @param address1
	 *            the address1 to set
	 */
	public void setAddress1(String address1) {
		beforeSet();
		this.address1 = address1;
	}

	/**
	 * @return the address2
	 */
	public String getAddress2() {
		beforeGet();
		return address2;
	}

	/**
	 * @param address2
	 *            the address2 to set
	 */
	public void setAddress2(String address2) {
		beforeSet();
		this.address2 = address2;
	}

	/**
	 * @return the postcode
	 */
	public String getPostcode() {
		beforeGet();
		return postcode;
	}

	/**
	 * @param postcode
	 *            the postcode to set
	 */
	public void setPostcode(String postcode) {
		beforeSet();
		this.postcode = postcode;
	}

	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		beforeGet();
		return telephone;
	}

	/**
	 * @param telephone
	 *            the telephone to set
	 */
	public void setTelephone(String telephone) {
		beforeSet();
		this.telephone = telephone;
	}

	/**
	 * @return the cellphone
	 */
	public String getCellphone() {
		beforeGet();
		return cellphone;
	}

	/**
	 * @param cellphone
	 *            the cellphone to set
	 */
	public void setCellphone(String cellphone) {
		beforeSet();
		this.cellphone = cellphone;
	}

	/**
	 * @return the fax
	 */
	public String getFax() {
		beforeGet();
		return fax;
	}

	/**
	 * @param fax
	 *            the fax to set
	 */
	public void setFax(String fax) {
		beforeSet();
		this.fax = fax;
	}

	/**
	 * @return the qQ
	 */
	public String getQQ() {
		beforeGet();
		return QQ;
	}

	/**
	 * @param qQ
	 *            the qQ to set
	 */
	public void setQQ(String qQ) {
		beforeSet();
		QQ = qQ;
	}

	/**
	 * @return the weibo
	 */
	public String getWeibo() {
		beforeGet();
		return weibo;
	}

	/**
	 * @param weibo
	 *            the weibo to set
	 */
	public void setWeibo(String weibo) {
		beforeSet();
		this.weibo = weibo;
	}

	/**
	 * @return the mailAddress
	 */
	public String getMailAddress() {
		beforeGet();
		return mailAddress;
	}

	/**
	 * @param mailAddress
	 *            the mailAddress to set
	 */
	public void setMailAddress(String mailAddress) {
		beforeSet();
		this.mailAddress = mailAddress;
	}

	/**
	 * @return the posX
	 */
	public Double getPosX() {
		beforeGet();
		return posX;
	}

	/**
	 * @param posX
	 *            the posX to set
	 */
	public void setPosX(Double posX) {
		beforeSet();
		this.posX = posX;
	}

	/**
	 * @return the posY
	 */
	public Double getPosY() {
		beforeGet();
		return posY;
	}

	/**
	 * @param posY
	 *            the posY to set
	 */
	public void setPosY(Double posY) {
		beforeSet();
		this.posY = posY;
	}

}
