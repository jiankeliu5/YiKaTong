package com.seedsoft.ykt.bean;

import java.io.Serializable;

/**
 * 根栏目下面子分类的组件类型实体类
 * */
public class FatherModuleBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2045287186635361404L;

//	private String refreshTime;//更新时间
	private String icon;//图标
	private String bg;//背景图
	private String name;//模块名称
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
