/**
 * 
 */
package com.changev.tutor.dto;

import java.util.ArrayList;
import java.util.List;

import com.changev.tutor.model.OrgInfoModel;
import com.changev.tutor.model.OrganizationModel;
import com.changev.tutor.model.TeacherModel;

/**
 * @author zhaoqing
 *机构主页dto
 */
public class OrgDTO {

	private List<TeacherModel> teachers = new ArrayList<TeacherModel>();
	private OrganizationModel org = null;
	private List<OrgInfoModel> newses = new ArrayList<OrgInfoModel>();
	private List<OrgInfoModel> activitys = new ArrayList<OrgInfoModel>();
	
	public List<TeacherModel> getTeachers() {
		return teachers;
	}
	public void setTeachers(List<TeacherModel> teachers) {
		this.teachers = teachers;
	}
	public OrganizationModel getOrg() {
		return org;
	}
	public void setOrg(OrganizationModel org) {
		this.org = org;
	}
	public List<OrgInfoModel> getNewses() {
		return newses;
	}
	public void setNewses(List<OrgInfoModel> newses) {
		this.newses = newses;
	}
	public List<OrgInfoModel> getActivitys() {
		return activitys;
	}
	public void setActivitys(List<OrgInfoModel> activitys) {
		this.activitys = activitys;
	}
	
	
}
