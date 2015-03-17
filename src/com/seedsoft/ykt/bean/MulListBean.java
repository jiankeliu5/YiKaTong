package com.seedsoft.ykt.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**组件类型
 * multtextlist
 * multimagetextlist
 * 的实体类
 * */
public class MulListBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4880765113397703279L;
	private ArrayList<MulChildrenBean> mulChildrenBeans;
	private ArrayList<AdBean> adBeans;
	public ArrayList<MulChildrenBean> getMulChildrenBeans() {
		return mulChildrenBeans;
	}
	public void setMulChildrenBeans(ArrayList<MulChildrenBean> mulChildrenBeans) {
		this.mulChildrenBeans = mulChildrenBeans;
	}
	public ArrayList<AdBean> getAdBeans() {
		return adBeans;
	}
	public void setAdBeans(ArrayList<AdBean> adBeans) {
		this.adBeans = adBeans;
	}
	
	
}
