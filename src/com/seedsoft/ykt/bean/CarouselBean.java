package com.seedsoft.ykt.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 组件类型
 * ("singletextlist")
 * ("singletextlist")
 * ("singleimagetextlist")
 * ("html5page")
 * ("carousel")
 * 的实体类
 * */
public class CarouselBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1395019625666257220L;
	private String url;
	private ArrayList<AdBean> adBeans;
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
