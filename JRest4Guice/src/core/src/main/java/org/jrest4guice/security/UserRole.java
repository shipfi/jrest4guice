package org.jrest4guice.security;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class UserRole implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1623972043510835563L;
	
	private User user;
	private List<Role> roles;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}
