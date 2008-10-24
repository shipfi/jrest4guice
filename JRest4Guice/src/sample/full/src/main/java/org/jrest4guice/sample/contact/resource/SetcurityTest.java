package org.jrest4guice.sample.contact.resource;

import java.util.List;

import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.Path;
import org.jrest4guice.rest.annotations.RESTful;
import org.jrest4guice.security.Role;
import org.jrest4guice.security.SecurityContext;
import org.jrest4guice.security.UserRole;

import com.google.inject.Inject;

@RESTful
@Path("/securityTest")
public class SetcurityTest {
	@Inject
	private SecurityContext securityContext;
	
	@Get
	public List<Role> listUserPrincipal(){
		System.out.println("==listUserPrincipal==");
		final UserRole userPrincipal = this.securityContext.getUserPrincipal();
		if(userPrincipal != null){
			for(Role role:userPrincipal.getRoles()){
				System.out.println(role.getName());
			}
			
			return userPrincipal.getRoles();
		}
		
		return null;
	}
}
