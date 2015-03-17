package com.seedsoft.ykt.bean;

import java.io.Serializable;

/**
 * 组件类型
 * 广告ad
 * 的实体类
 * */
public class AdBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8942458372857282911L;
	private String title;
	private String type;
	private String image;
	private String url;
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
	
	
	
}
