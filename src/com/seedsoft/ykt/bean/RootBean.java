package com.seedsoft.ykt.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *	根栏目实体类
 * 
 * */
public class RootBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8824031448851925178L;
	private String name;
	private String frontImage;
	private String backImage;
	private String backColor;
	private String showType;
	private boolean isChanged = false;
	private ArrayList<FatherBean> fatherBeans;
	
	
	public String getShowType() {
		return showType;
	}
	public void setShowType(String showType) {
		this.showType = showType;
	}
	public boolean isChanged() {
		return isChanged;
	}
	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFrontImage() {
		return frontImage;
	}
	public void setFrontImage(String frontImage) {
		this.frontImage = frontImage;
	}
	
	public String getBackImage() {
		return backImage;
	}
	public void setBackImage(String backImage) {
		this.backImage = backImage;
	}
	public String getBackColor() {
		return backColor;
	}
	public void setBackColor(String backColor) {
		this.backColor = backColor;
	}
	public ArrayList<FatherBean> getFatherBeans() {
		return fatherBeans;
	}
	public void setFatherBeans(ArrayList<FatherBean> fatherBeans) {
		this.fatherBeans = fatherBeans;
	}

	
}
