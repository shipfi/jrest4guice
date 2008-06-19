package org.jrest4guice.sample.contact.security;

public class UserSecurityInfo {
	private String userName;
	private String userRoles;
	
	public UserSecurityInfo(String userName, String userRoles) {
		super();
		this.userName = userName;
		this.userRoles = userRoles;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(String userRoles) {
		this.userRoles = userRoles;
	}
}
