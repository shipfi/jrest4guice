package org.jrest4guice.sample.resources;

import java.util.ArrayList;
import java.util.List;

import org.jrest4guice.annotation.Get;
import org.jrest4guice.annotation.Restful;
import org.jrest4guice.core.security.Role;
import org.jrest4guice.sample.service.UserManageService;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 * 
 */
@Restful(uri = { "/userRole" })
public class UserRoleResource {
	@Inject
	private UserManageService service;

	@Get
	public List<Role> authUser(String userName) {
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
