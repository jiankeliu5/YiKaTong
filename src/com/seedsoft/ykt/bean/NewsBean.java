package com.seedsoft.ykt.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class NewsBean implements Serializable{

	private static final long serialVersionUID = 7645328024442917848L;
	private String pageNum;//本新闻所在的xml总页数
	private String title;//标题
	private String author;//作者
	private String image;//图片地址
	private String desp;//描述
	private String date;//时间
	private String url;//文章内容html链接
	private String source;//来源
	private String location;//地理信息
	private String vedio;//视频播放地址
	
	private String id;//编号
	private String isAllowComment;//是否允许评论
	private String commentNum;//评论数目
	private String commentURL;//评论链接xml
	
	private ArrayList<AdBean> adBeans;//广告
	
	
	
	
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
