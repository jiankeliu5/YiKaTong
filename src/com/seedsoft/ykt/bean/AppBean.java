package com.seedsoft.ykt.bean;

import java.io.Serializable;

/**
 * 组件类型
 * imageapp
 * textapp
 * 的实体类
 * */
public class AppBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5720067776632449906L;
	private String title;
	private String type;
	private String image;
	private String url;
	private String path;//包名
	private String size;
	
	
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
}
