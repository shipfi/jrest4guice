package org.cnoss.cms.entity;



import org.cnoss.cms.entity.audit.AuditableEntity;



public class SpecialComment extends AuditableEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8406598478934733689L;
	private String specialId;
	private String username;
	private String email;
	private String http;
	private String text;
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
	public String getSpecialId() {
		return specialId;
	}
	public void setSpecialId(String specialId) {
		this.specialId = specialId;
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
