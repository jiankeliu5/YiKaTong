package com.seedsoft.ykt.bean;

import java.io.Serializable;

public class CommentBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7244576337923044060L;
	private String author;
	private String forID;
	private String iconURL;
	private String likeNUM;
	private String commentContent;
	private String commentTime;
	private String commentLoction;
	
	
	public String getCommentLoction() {
		return commentLoction;
	}
	public void setCommentLoction(String commentLoction) {
		this.commentLoction = commentLoction;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getForID() {
		return forID;
	}
	public void setForID(String forID) {
		this.forID = forID;
	}
	public String getIconURL() {
		return iconURL;
	}
	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}
	public String getLikeNUM() {
		return likeNUM;
	}
	public void setLikeNUM(String likeNUM) {
		this.likeNUM = likeNUM;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public String getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}
	
	
}
