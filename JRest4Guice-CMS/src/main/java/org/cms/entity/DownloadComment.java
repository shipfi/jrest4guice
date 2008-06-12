package org.cms.entity;

import java.util.Date;

import org.jrest4guice.jpa.audit.AuditableEntity;



/**
 * DownloadComment.java
 * 
 */

public class DownloadComment extends AuditableEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6072262512361577769L;
	private int downloadId;
	private String username;
	private String email;
	private String http;
	private String ip;	
	private String text;
	private Date dateCreated;
	
	private Download download;
	public Download getDownload() {
		return download;
	}
	public void setDownload(Download download) {
		this.download = download;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public int getDownloadId() {
		return downloadId;
	}
	public void setDownloadId(int downloadId) {
		this.downloadId = downloadId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHttp() {
		return http;
	}
	public void setHttp(String http) {
		this.http = http;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

}
