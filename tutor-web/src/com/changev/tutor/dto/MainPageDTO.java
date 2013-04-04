/**
 * 
 */
package com.changev.tutor.dto;

import java.util.ArrayList;
import java.util.List;

import com.changev.tutor.model.FaqModel;
import com.changev.tutor.model.OrganizationModel;
import com.changev.tutor.model.PictureModel;

/**
 * @author zhaoqing
 *
 */
public class MainPageDTO {

	private List<FaqModel> faqs = new ArrayList<FaqModel>();
	
	private List<FaqModel> newses = new ArrayList<FaqModel>();
	
	private List<PictureModel> advs = new ArrayList<PictureModel>();
	
	private List<FaqModel> activities = new ArrayList<FaqModel>();
	
	private List<OrganizationModel> orgs = new ArrayList<OrganizationModel>();
	
	
	public List<OrganizationModel> getOrgs() {
		return orgs;
	}
	
	public void setOrgs(List<OrganizationModel> orgs) {
		this.orgs = orgs;
	}

	public List<FaqModel> getFaqs() {
		return faqs;
	}

	public void setFaqs(List<FaqModel> faqs) {
		this.faqs = faqs;
	}

	public List<PictureModel> getAdvs() {
		return advs;
	}

	public void setAdvs(List<PictureModel> advs) {
		this.advs = advs;
	}
	
	public void setNewses(List<FaqModel> newses) {
		this.newses = newses;
	}
	
	public List<FaqModel> getNewses() {
		return newses;
	}
	
	public List<FaqModel> getActivities() {
		return activities;
	}
	
	public void setActivities(List<FaqModel> activities) {
		this.activities = activities;
	}
	
}
