package org.jrest4guice.sample.resources;

import org.jrest4guice.annotation.Get;
import org.jrest4guice.annotation.ModelBean;
import org.jrest4guice.annotation.Parameter;
import org.jrest4guice.annotation.Post;
import org.jrest4guice.annotation.Restful;
import org.jrest4guice.sample.entity.User;
import org.jrest4guice.sample.service.UserManageService;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 * 
 */
@Restful(uri = { "/user", "/user/{userId}" })
public class UserResource {
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
}
