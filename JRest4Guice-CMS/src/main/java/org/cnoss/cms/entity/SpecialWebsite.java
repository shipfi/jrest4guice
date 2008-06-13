package org.cnoss.cms.entity;


import org.cnoss.cms.entity.audit.AuditableEntity;



public class SpecialWebsite extends AuditableEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5149192754809161108L;
	private String specialId;
	private String name;
	private String url;
	
	public String getSpecialId() {
		return specialId;
	}
	public void setSpecialId(String specialId) {
		this.specialId = specialId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}
