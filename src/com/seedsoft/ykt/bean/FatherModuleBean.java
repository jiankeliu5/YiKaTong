package com.seedsoft.ykt.bean;

import java.io.Serializable;

/**
 * ����Ŀ�����ӷ�����������ʵ����
 * */
public class FatherModuleBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2045287186635361404L;

//	private String refreshTime;//����ʱ��
	private String icon;//ͼ��
	private String bg;//����ͼ
	private String name;//ģ������
	private String type;
	private Object objects;
	
	
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getBg() {
		return bg;
	}
	public void setBg(String bg) {
		this.bg = bg;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Object getObjects() {
		return objects;
	}
	public void setObjects(Object objects) {
		this.objects = objects;
	}
	
	
}
