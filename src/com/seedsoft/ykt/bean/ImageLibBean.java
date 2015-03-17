package com.seedsoft.ykt.bean;

import java.io.Serializable;

public class ImageLibBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2662443917669013368L;
	private String id;
	private String name;
	private String parent;
	private String url;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	
}
