package com.seedsoft.ykt.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class NewsBean implements Serializable{

	private static final long serialVersionUID = 7645328024442917848L;
	private String pageNum;//���������ڵ�xml��ҳ��
	private String title;//����
	private String author;//����
	private String image;//ͼƬ��ַ
	private String desp;//����
	private String date;//ʱ��
	private String url;//��������html����
	private String source;//��Դ
	private String location;//������Ϣ
	private String vedio;//��Ƶ���ŵ�ַ
	
	private String id;//���
	private String isAllowComment;//�Ƿ���������
	private String commentNum;//������Ŀ
	private String commentURL;//��������xml
	
	private ArrayList<AdBean> adBeans;//���
	
	
	
	
	public String getPageNum() {
		return pageNum;
	}
	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}
	public ArrayList<AdBean> getAdBeans() {
		return adBeans;
	}
	public void setAdBeans(ArrayList<AdBean> adBeans) {
		this.adBeans = adBeans;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIsAllowComment() {
		return isAllowComment;
	}
	public void setIsAllowComment(String isAllowComment) {
		this.isAllowComment = isAllowComment;
	}
	public String getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(String commentNum) {
		this.commentNum = commentNum;
	}
	public String getCommentURL() {
		return commentURL;
	}
	public void setCommentURL(String commentURL) {
		this.commentURL = commentURL;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDesp() {
		return desp;
	}
	public void setDesp(String desp) {
		this.desp = desp;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getVedio() {
		return vedio;
	}
	public void setVedio(String vedio) {
		this.vedio = vedio;
	}
	
	
}
