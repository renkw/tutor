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
	private String province;
	private String city;
	private String district;
	private String address;
	private String postcode;
	private String telephone;
	private String cellphone;
	private String fax;
	private String QQ;
	private String weibo;
	private String mailAddress;
	private Double posX;
	private Double posY;

	public void clone(UserContactModel another) {
		super.clone(another);
		this.setName(another.getName());
		this.setProvince(another.getProvince());
		this.setCity(another.getCity());
		this.setDistrict(another.getDistrict());
		this.setAddress(another.getAddress());
		this.setPostcode(another.getPostcode());
		this.setTelephone(another.getTelephone());
		this.setCellphone(another.getCellphone());
		this.setFax(another.getFax());
		this.setQQ(another.getQQ());
		this.setWeibo(another.getWeibo());
		this.setMailAddress(another.getMailAddress());
		this.setPosX(another.getPosX());
		this.setPosY(another.getPosY());
	}

	@Override
	public UserContactModel clone() {
		return (UserContactModel) super.clone();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @param province
	 *            the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * @param district
	 *            the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the postcode
	 */
	public String getPostcode() {
		return postcode;
	}

	/**
	 * @param postcode
	 *            the postcode to set
	 */
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone
	 *            the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @return the cellphone
	 */
	public String getCellphone() {
		return cellphone;
	}

	/**
	 * @param cellphone
	 *            the cellphone to set
	 */
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	/**
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax
	 *            the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return the qQ
	 */
	public String getQQ() {
		return QQ;
	}

	/**
	 * @param qQ
	 *            the qQ to set
	 */
	public void setQQ(String qQ) {
		QQ = qQ;
	}

	/**
	 * @return the weibo
	 */
	public String getWeibo() {
		return weibo;
	}

	/**
	 * @param weibo
	 *            the weibo to set
	 */
	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}

	/**
	 * @return the mailAddress
	 */
	public String getMailAddress() {
		return mailAddress;
	}

	/**
	 * @param mailAddress
	 *            the mailAddress to set
	 */
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	/**
	 * @return the posX
	 */
	public Double getPosX() {
		return posX;
	}

	/**
	 * @param posX the posX to set
	 */
	public void setPosX(Double posX) {
		this.posX = posX;
	}

	/**
	 * @return the posY
	 */
	public Double getPosY() {
		return posY;
	}

	/**
	 * @param posY the posY to set
	 */
	public void setPosY(Double posY) {
		this.posY = posY;
	}

}
