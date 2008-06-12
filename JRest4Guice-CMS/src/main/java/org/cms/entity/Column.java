package org.cms.entity;



import org.jrest4guice.jpa.audit.AuditableEntity;



public class Column extends  AuditableEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1234905539044660999L;
	private String name;
	private String path;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
