package com.seedsoft.ykt.bean;

import java.io.Serializable;

/**
 * mulbean����������
 * �ӷ���ʵ����
 * */
public class MulChildrenBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3086434056604479845L;
	private String title;
	private String url;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
