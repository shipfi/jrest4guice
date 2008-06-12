package org.cms.entity;

import java.util.Date;

import org.jrest4guice.jpa.audit.AuditableEntity;



/**
 * 新闻实体类
 * 
 */
public class News extends AuditableEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6255479910828967446L;
	private String title;
	private String content;
	private String username;
	private int state;	
	private Date dateCreated;	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
}
