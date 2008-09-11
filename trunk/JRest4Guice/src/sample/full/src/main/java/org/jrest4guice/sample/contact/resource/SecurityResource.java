package org.jrest4guice.sample.contact.resource;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jrest4guice.commons.http.CookieUtil;
import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.Path;
import org.jrest4guice.sample.contact.security.UserSecurityInfo;
import org.jrest4guice.sample.contact.service.UserManageService;
import org.jrest4guice.security.Role;
import org.jrest4guice.security.User;
import org.jrest4guice.security.UserRole;
import org.jrest4guice.sna.CacheProvider;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@Path("/security")
public class SecurityResource {
	@Inject
	private UserManageService domain;

	@Inject
	HttpServletRequest request;

	@Inject
	HttpServletResponse response;

	@Inject(optional = true)
	private CacheProvider cacheProvider;

	@Get
	@Path("auth")
	public boolean authUser(String userName, String userPassword) {
		boolean result = this.domain.authUser(userName, userPassword);
		if (result) {
			UserRole userRole = new UserRole();
			User user = new User();
			List<Role> roles = this.listUserRoles(userName);
			userRole.setUser(user);
			userRole.setRoles(roles);
			
			cacheProvider.put(CacheProvider.USER_PRINCIPAL_CACHE_KEY_PREFIX+userName, userRole);
		}
		return result;
	}

	@Get
	@Path("userRoles")
	public UserSecurityInfo listCurrentUserRoles() {
		Principal userPrincipal = this.request.getUserPrincipal();
		if (userPrincipal != null) {
			String name = userPrincipal.getName();
			List<Role> roles = this.listUserRoles(name);
			List<String> roleNames = new ArrayList<String>();
			for (Role r : roles)
				roleNames.add(r.getName());

			UserSecurityInfo result = new UserSecurityInfo(name, StringUtils
					.join(roleNames, ","));
			return result;
		}

		return null;
	}

	@Get
	@Path("{userName}/roles")
	public List<Role> listUserRoles(String userName) {
		List<org.jrest4guice.sample.contact.entity.Role> userRoles = this.domain
				.getUserRoles(userName);
		List<Role> roles = new ArrayList<Role>(userRoles.size());
		Role role;
		for (org.jrest4guice.sample.contact.entity.Role _role : userRoles) {
			role = new Role();
			role.setId(_role.getId());
			role.setName(_role.getName());
			roles.add(role);
		}
		return roles;
	}

}
