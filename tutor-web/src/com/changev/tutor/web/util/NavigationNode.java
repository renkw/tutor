/*
 * File   NavigationNode.java
 * Create 2013/01/06
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web.util;

import java.util.List;

/**
 * @author ren
 * 
 */
public class NavigationNode {

	private String linkUrl;
	private String label;
	private String description;
	private NavigationNode parentNode;
	private List<NavigationNode> childNodes;

	public boolean lookup(String url, List<NavigationNode> stack) {
		if (url.equals(linkUrl))
			return true;
		if (childNodes != null && !childNodes.isEmpty()) {
			for (NavigationNode child : childNodes) {
				if (child.lookup(url, stack)) {
					stack.add(child);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @return the linkUrl
	 */
	public String getLinkUrl() {
		return linkUrl;
	}

	/**
	 * @param linkUrl
	 *            the linkUrl to set
	 */
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the parentNode
	 */
	public NavigationNode getParentNode() {
		return parentNode;
	}

	/**
	 * @return the childNodes
	 */
	public List<NavigationNode> getChildNodes() {
		return childNodes;
	}

	/**
	 * @param childNodes
	 *            the childNodes to set
	 */
	public void setChildNodes(List<NavigationNode> childNodes) {
		this.childNodes = childNodes;
		if (childNodes != null && !childNodes.isEmpty()) {
			for (NavigationNode child : childNodes) {
				child.parentNode = this;
			}
		}
	}

}
