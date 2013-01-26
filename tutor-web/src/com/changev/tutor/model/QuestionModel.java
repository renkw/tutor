/*
 * File   QuestionModel.java
 * Create 2013/01/12
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.changev.tutor.Tutor;
import com.db4o.ObjectContainer;
import com.db4o.collections.ActivatableArrayList;
import com.db4o.config.annotations.Indexed;

/**
 * <p>
 * 提问。
 * </p>
 * <p>
 * 2013-1-19 添加问题类型，第一期实现试卷类型<br>
 * <ul>
 * <li>试卷提问类型</li>
 * <li>批改作业类型</li>
 * <li></li>
 * </ul>
 * </p>
 * 
 * @author ren
 * 
 */
public class QuestionModel extends AbstractModel {

	private static final long serialVersionUID = -1143983407388574974L;

	public static final String QUESTIONER = "questioner";
	public static final String STUDENT = "student";
	public static final String PROVINCE = "province";
	public static final String CITY = "city";
	public static final String DISTRICT = "district";
	public static final String SUBJECT = "subject";
	public static final String GRADE = "grade";
	public static final String GRADE_LEVEL = "gradeLevel";
	public static final String TITLE = "title";
	public static final String ASSIGN_TO = "assignTo";
	public static final String FINAL_ANSWERER = "finalAnswerer";
	public static final String CLOSED = "closed";
	public static final String EXPIRATION_DATE = "expirationDate";
	public static final String CLOSED_DATE_TIME = "closedDateTime";
	public static final String UPLOAD_PICTURES = "uploadPictures";

	@Indexed
	private UserModel questioner;
	private StudentModel student;
	private String province;
	// XXX 有user的model为什么还需要城市这些？是不是可以去掉
	private String city;
	private String district;
	private String subject;
	private String grade;
	private Byte gradeLevel;
	private String title;
	@Indexed
	private TeacherModel assignTo;
	private TeacherModel finalAnswerer;
	private Boolean closed;
	private Date expirationDate;
	private Date closedDateTime;
	@Indexed
	private List<String> uploadPictures;
	/*
	 * 问题类型
	 */
	private int type;
	/*
	 * 提问人的id
	 */
	private String user_id;
	
	private transient boolean newFlag;

	public String getLocation() {
		beforeGet();
		return new StringBuilder().append(StringUtils.defaultString(province))
				.append(StringUtils.defaultString(city))
				.append(StringUtils.defaultString(district)).toString();
	}

	@Override
	public void objectOnActivate(ObjectContainer container) {
		super.objectOnActivate(container);
	}

	@Override
	public void objectOnNew(ObjectContainer container) {
		super.objectOnNew(container);
		setProvince(student.getProvince());
		setCity(student.getCity());
		setDistrict(student.getDistrict());
		setGrade(student.getGrade());
		setGradeLevel(student.getGradeLevel());
		setClosed(Boolean.FALSE);
		Calendar today = Tutor.currentDateCalendar();
		today.add(Calendar.MONTH, 1);
		setExpirationDate(today.getTime());
	}

	@Override
	public void objectOnUpdate(ObjectContainer container) {
		super.objectOnUpdate(container);
	}

	@Override
	public QuestionModel clone() {
		return (QuestionModel) super.clone();
	}

	/**
	 * @return the questioner
	 */
	public UserModel getQuestioner() {
		beforeGet();
		return questioner;
	}

	/**
	 * @param questioner
	 *            the questioner to set
	 */
	public void setQuestioner(UserModel questioner) {
		beforeSet();
		this.questioner = questioner;
	}

	/**
	 * @return the student
	 */
	public StudentModel getStudent() {
		beforeGet();
		return student;
	}

	/**
	 * @param student
	 *            the student to set
	 */
	public void setStudent(StudentModel student) {
		beforeSet();
		this.student = student;
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		beforeGet();
		return province;
	}

	/**
	 * @param province
	 *            the province to set
	 */
	public void setProvince(String province) {
		beforeSet();
		this.province = province;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		beforeGet();
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		beforeSet();
		this.city = city;
	}

	/**
	 * @return the district
	 */
	public String getDistrict() {
		beforeGet();
		return district;
	}

	/**
	 * @param district
	 *            the district to set
	 */
	public void setDistrict(String district) {
		beforeSet();
		this.district = district;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		beforeGet();
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		beforeSet();
		this.subject = subject;
	}

	/**
	 * @return the grade
	 */
	public String getGrade() {
		beforeGet();
		return grade;
	}

	/**
	 * @param grade
	 *            the grade to set
	 */
	public void setGrade(String grade) {
		beforeSet();
		this.grade = grade;
	}

	/**
	 * @return the gradeLevel
	 */
	public Byte getGradeLevel() {
		beforeGet();
		return gradeLevel;
	}

	/**
	 * @param gradeLevel
	 *            the gradeLevel to set
	 */
	public void setGradeLevel(Byte gradeLevel) {
		beforeSet();
		this.gradeLevel = gradeLevel;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		beforeGet();
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		beforeSet();
		this.title = title;
	}

	/**
	 * @return the assignTo
	 */
	public TeacherModel getAssignTo() {
		beforeGet();
		return assignTo;
	}

	/**
	 * @param assignTo
	 *            the assignTo to set
	 */
	public void setAssignTo(TeacherModel assignTo) {
		beforeSet();
		this.assignTo = assignTo;
	}

	/**
	 * @return the expirationDate
	 */
	public Date getExpirationDate() {
		beforeGet();
		return expirationDate;
	}

	/**
	 * @param expirationDate
	 *            the expirationDate to set
	 */
	public void setExpirationDate(Date expirationDate) {
		beforeSet();
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the closedDateTime
	 */
	public Date getClosedDateTime() {
		beforeGet();
		return closedDateTime;
	}

	/**
	 * @param closedDateTime
	 *            the closedDateTime to set
	 */
	public void setClosedDateTime(Date closedDateTime) {
		beforeSet();
		this.closedDateTime = closedDateTime;
	}

	/**
	 * @return the closed
	 */
	public Boolean getClosed() {
		beforeGet();
		return closed;
	}

	/**
	 * @param closed
	 *            the closed to set
	 */
	public void setClosed(Boolean closed) {
		beforeSet();
		this.closed = closed;
	}

	/**
	 * @return the finalAnswerer
	 */
	public TeacherModel getFinalAnswerer() {
		beforeGet();
		return finalAnswerer;
	}

	/**
	 * @param finalAnswerer
	 *            the finalAnswerer to set
	 */
	public void setFinalAnswerer(TeacherModel finalAnswerer) {
		beforeSet();
		this.finalAnswerer = finalAnswerer;
	}

	/**
	 * @return the uploadPictures
	 */
	public List<String> getUploadPictures() {
		beforeGet();
		return uploadPictures;
	}

	/**
	 * @return the uploadPictures
	 */
	public List<String> getUploadPicturesFor() {
		beforeGet();
		if (uploadPictures == null) {
			beforeSet();
			uploadPictures = new ActivatableArrayList<String>();
		}
		return uploadPictures;
	}

	/**
	 * @return the newFlag
	 */
	public boolean isNewFlag() {
		return newFlag;
	}

	/**
	 * @param newFlag the newFlag to set
	 */
	public void setNewFlag(boolean newFlag) {
		this.newFlag = newFlag;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
}
