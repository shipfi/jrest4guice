package org.cms.entity;

import java.util.Date;

import org.cms.entity.Content;
import org.jrest4guice.jpa.audit.AuditableEntity;



public class ContentComment extends AuditableEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7403287834838003728L;
	private Long contentId;
	private String username;
	private String email;
	private String website;
	private String ip;
	private int up;
	private int down;
	private String title;
	private String text;
	private Date dateCreated;
	
	private Content content;
	public Content getContent() {
		return content;
	}
	public void setContent(Content content) {
		this.content = content;
	}

	public Long getContentId() {
		return contentId;
	}
	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public int getDown() {
		return down;
	}
	public void setDown(int down) {
		this.down = down;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getUp() {
		return up;
	}
	public void setUp(int up) {
		this.up = up;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	} 
	
}
