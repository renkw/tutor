/**
 * 
 */
package com.changev.tutor.model;

import com.db4o.config.annotations.Indexed;

/**
 * @author zhaoqing
 *
 */
public class FaqModel extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4280149922663712773L;

	@Indexed
	private long id;
	private String title;
	private String content;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
}
