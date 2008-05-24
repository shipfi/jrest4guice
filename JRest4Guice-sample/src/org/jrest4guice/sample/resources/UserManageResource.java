package org.jrest4guice.sample.resources;

import java.util.ArrayList;
import java.util.List;

import org.jrest4guice.annotation.Get;
import org.jrest4guice.annotation.ModelBean;
import org.jrest4guice.annotation.Path;
import org.jrest4guice.annotation.Post;
import org.jrest4guice.core.security.Role;
import org.jrest4guice.sample.entity.User;
import org.jrest4guice.sample.service.UserManageService;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 * 
 */
@Path({"/user"})
public class UserManageResource {
	@Inject
	private UserManageService service;

	@Post
	public String createContact(@ModelBean
	User user) {
		return null;
	}

	@Get
	public boolean authUser(String userName, String userPassword) {
		boolean result = this.service.authUser(userName, userPassword);
		return result;
	}

	@Get
	@Path({"roles","{userName}/roles"})
	public List<Role> listUserRolse(String userName) {
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
