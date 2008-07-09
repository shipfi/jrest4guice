package org.jrest4guice.sample.contact.resource;

import javax.servlet.http.HttpServletRequest;

import org.jrest4guice.client.Page;
import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.ModelBean;
import org.jrest4guice.rest.annotations.Path;
import org.jrest4guice.rest.annotations.Post;
import org.jrest4guice.sample.contact.entity.Role;
import org.jrest4guice.sample.contact.entity.User;
import org.jrest4guice.sample.contact.service.UserManageService;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@Path( { "/user" })
public class UserManageResource {
	@Inject
	private UserManageService domain;

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
		return this.domain.findUser(userName);
	}

	@Get
	@Path("{userName}/roles")
	public Page<Role> listCurrentUserRolse(int pageIndex, int pageSize) {
		return this.domain.getAllRoles(pageIndex, pageSize);
	}
}
