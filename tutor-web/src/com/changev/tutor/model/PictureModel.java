/**
 * 
 */
package com.changev.tutor.model;

import com.db4o.config.annotations.Indexed;

/**
 * @author zhaoqing
 *mainpage 用来存闪烁图片的
 */
public class PictureModel extends AbstractModel {

	private static final long serialVersionUID = -6441793382430288321L;
	//排序
	private int index;
	private String title;
	private String url;
	private String link2;
	@Indexed
	private long id;
	@Indexed
	private long owener;
	
	public PictureModel() {
	}
	
	public PictureModel(String id){
		this.id = Long.parseLong(id);
	}
	
	public int getIndex() {
		beforeGet();
		return index;
	}
	public void setIndex(int index) {
		beforeSet();
		this.index = index;
	}
	public String getTitle() {
		beforeGet();
		return title;
	}
	public void setTitle(String title) {
		beforeSet();
		this.title = title;
	}
	public String getUrl() {
		beforeGet();
		return url;
	}
	public void setUrl(String url) {
		beforeSet();
		this.url = url;
	}
	public String getLink2() {
		beforeGet();
		return link2;
	}
	public void setLink2(String link2) {
		beforeSet();
		this.link2 = link2;
	}
	
	public long getId() {
		beforeGet();
		return id;
	}
	
	public void setId(long id) {
		beforeSet();
		this.id = id;
	}
	
	public long getOwener() {
		beforeGet();
		return owener;
	}
	
	public void setOwener(long owener) {
		beforeSet();
		this.owener = owener;
	}
}
