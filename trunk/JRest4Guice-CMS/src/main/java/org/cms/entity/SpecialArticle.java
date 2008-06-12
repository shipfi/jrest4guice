package org.cms.entity;



import org.jrest4guice.jpa.audit.AuditableEntity;



public class SpecialArticle extends AuditableEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8076617607513340330L;
	private int specialId;
	private String title;
	private String url;
	private String dateCreated;
	
	private Special special;
	public Special getSpecial() {
		return special;
	}
	public void setSpecial(Special special) {
		this.special = special;
	}
	
	public int getSpecialId() {
		return specialId;
	}
	public void setSpecialId(int specialId) {
		this.specialId = specialId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

}
