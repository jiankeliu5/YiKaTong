package com.seedsoft.ykt.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 根组件下小分类的实体类
 * */
public class FatherBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8944182256808013367L;
	private String name;
	private String isShow;
	private boolean isNew = false;
	private String xmlData;
	private String frontImage;
	private String backColor;
	private ArrayList<FatherModuleBean> fatherModuleBeans;

	
	public String getFrontImage() {
		return frontImage;
	}
	public void setFrontImage(String frontImage) {
		this.frontImage = frontImage;
	}
	public String getBackColor() {
		return backColor;
	}
	public void setBackColor(String backColor) {
		this.backColor = backColor;
	}
	public boolean isNew() {
		return isNew;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	
	public String getXmlData() {
		return xmlData;
	}
	public void setXmlData(String xmlData) {
		this.xmlData = xmlData;
	}
	public ArrayList<FatherModuleBean> getFatherModuleBeans() {
		return fatherModuleBeans;
	}
	public void setFatherModuleBeans(ArrayList<FatherModuleBean> fatherModuleBeans) {
		this.fatherModuleBeans = fatherModuleBeans;
	}
	
	
}
