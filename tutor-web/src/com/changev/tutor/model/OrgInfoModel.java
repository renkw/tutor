/**
 * 
 */
package com.changev.tutor.model;

import com.db4o.config.annotations.Indexed;

/**
 * @author zhaoqing
 *
 */
public class OrgInfoModel extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4280149922663712773L;

	@Indexed
	private long id;
	private String title;
	private String content;
	private String tags;
	/**
	 * 0  faq
	 * 1  news
	 * 2  activity
	 */
	@Indexed
	private int type = 0;
	
	/**
	 * 和机构email关联
	 */
	@Indexed
	private long owerner;
	
	
	public OrgInfoModel() {
	}

	public OrgInfoModel(String id) {
		this.id = Long.parseLong(id);
	}
	
	public int getType() {
		beforeGet();
		return type;
	}
	
	public void setType(int type) {
		beforeSet();
		this.type = type;
	}
	
	public String getTags() {
		beforeGet();
		return tags;
	}
	
	public void setTags(String tags) {
		beforeSet();
		this.tags = tags;
	}
	
	public long getId() {
		beforeGet();
		return id;
	}
	
	public void setId(long id) {
		beforeSet();
		this.id = id;
	}
	
	public String getTitle() {
		beforeGet();
		return title;
	}
	
	public void setTitle(String title) {
		beforeSet();
		this.title = title;
	}
	
	public String getContent() {
		beforeGet();
		return content;
	}
	
	public void setContent(String content) {
		beforeSet();
		this.content = content;
	}
	
	public long getOwerner() {
		beforeGet();
		return owerner;
	}
	
	public void setOwerner(long owerner) {
		beforeSet();
		this.owerner = owerner;
	}
}
