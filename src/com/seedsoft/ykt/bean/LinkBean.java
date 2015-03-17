package com.seedsoft.ykt.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 组件类型
 * imagelink
 * textlink
 * 的实体类
 * */
public class LinkBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4524100071024382830L;
	private String title;
	private String type;
	private String image;
	private String url;
	private ArrayList<AdBean> adBeans;
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
	public ArrayList<AdBean> getAdBeans() {
		return adBeans;
	}
	public void setAdBeans(ArrayList<AdBean> adBeans) {
		this.adBeans = adBeans;
	}
	
	
	
}
