package org.cms.entity;

import java.util.Map;

import org.jrest4guice.jpa.audit.AuditableEntity;



public class Role extends AuditableEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 904477398643579974L;
	private String name;
	private String description;
	private String permissions;
	
	private Map permissionMap;
	public Map getPermissionMap() {
		return permissionMap;
	}
	public void setPermissionMap(Map permissionMap) {
		this.permissionMap = permissionMap;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPermissions() {
		return permissions;
	}
	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}
}
