package com.seedsoft.ykt.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * �������
 * ("singletextlist")
 * ("singletextlist")
 * ("singleimagetextlist")
 * ("html5page")
 * ("carousel")
 * ��ʵ����
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
