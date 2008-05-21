package org.jrest4guice.security;

import java.util.Map;
import java.util.Set;

import org.jrest4guice.core.guice.GuiceContext;

import com.google.inject.Singleton;

@Singleton
public class SecurityManager {
	
	public static final String USER = "current_security_user";
	
	private Map<User, Set<Role>> userRols;
	
	public void addUserRole(User user,Set<Role> roles){
	}
	
	public void clearUserRole(User user){
	}
	
	public boolean hasRole(User user,String[] roleNames){
		if(!this.userRols.containsKey(user))
			return false;
		
		boolean result = false;
		
		Set<Role> roles = this.userRols.get(user);
		
		top:
		for(Role role :roles){
			for(String roleName:roleNames){
				if(role.getName().equalsIgnoreCase(roleName)){
					result = true;
					break top;
				}
			}
		}
		
		return result;
	}
	
	public static SecurityManager getInstance(){
		return GuiceContext.getInstance().getBean(SecurityManager.class);
	}
}
