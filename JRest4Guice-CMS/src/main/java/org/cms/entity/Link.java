package org.cms.entity;



import org.jrest4guice.jpa.audit.AuditableEntity;



public class Link extends AuditableEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4757810319979911843L;
	private String name;
	private String url;
	private int isLogo;
	private String description;
	private int displayOrder;
	private int colummId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getColummId() {
		return colummId;
	}

	public void setColummId(int colummId) {
		this.colummId = colummId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public int getIsLogo() {
		return isLogo;
	}

	public void setIsLogo(int isLogo) {
		this.isLogo = isLogo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	

}
