package org.jrest4guice.sample.resources;

import javax.servlet.http.HttpServletRequest;

import org.jrest4guice.client.Page;
import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.ModelBean;
import org.jrest4guice.rest.annotations.Path;
import org.jrest4guice.rest.annotations.Post;
import org.jrest4guice.sample.entity.Role;
import org.jrest4guice.sample.entity.User;
import org.jrest4guice.sample.service.UserManageService;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ：86895156)</a>
 * 
 */
@Path( { "/user" })
public class UserManageResource {
	@Inject
	private UserManageService service;

	@Inject
	HttpServletRequest request;

	@Post
	public String createContact(@ModelBean
	User user) {
		return null;
	}

	@Get
	@Path("{userName}")
	public User findUser(String userName) {
		return this.service.findUser(userName);
	}

	@Get
	@Path("{userName}/roles")
	public Page<Role> listCurrentUserRolse(int pageIndex, int pageSize) {
		return this.service.getAllRoles(pageIndex, pageSize);
	}
}
