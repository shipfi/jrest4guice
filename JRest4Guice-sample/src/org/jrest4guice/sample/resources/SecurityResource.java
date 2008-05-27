package org.jrest4guice.sample.resources;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jrest4guice.annotations.Get;
import org.jrest4guice.annotations.Path;
import org.jrest4guice.core.security.Role;
import org.jrest4guice.sample.security.UserSecurityInfo;
import org.jrest4guice.sample.service.UserManageService;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 * 
 */
@Path("/security")
public class SecurityResource {
	@Inject
	private UserManageService service;
	
	@Inject
	HttpServletRequest request;

	@Get
	@Path("auth")
	public boolean authUser(String userName, String userPassword) {
		boolean result = this.service.authUser(userName, userPassword);
		return result;
	}

	@Get
	@Path("userRoles")
	public UserSecurityInfo listCurrentUserRoles() {
		Principal userPrincipal = this.request.getUserPrincipal();
		if(userPrincipal != null){
			String name = userPrincipal.getName();
			List<Role> roles = this.listUserRoles(name);
			List<String> roleNames = new ArrayList<String>();
			for(Role r :roles)
				roleNames.add(r.getName());

			UserSecurityInfo result = new UserSecurityInfo(name,StringUtils.join(roleNames,","));
			return result;
		}
		
		return null;
	}
	
	@Get
	@Path("{userName}/roles")
	public List<Role> listUserRoles(String userName) {
		List<org.jrest4guice.sample.entity.Role> userRoles = this.service
				.getUserRoles(userName);
		List<Role> roles = new ArrayList<Role>(userRoles.size());
		Role role;
		for (org.jrest4guice.sample.entity.Role _role : userRoles) {
			role = new Role();
			role.setId(_role.getId());
			role.setName(_role.getName());
			roles.add(role);
		}
		return roles;
	}

}
