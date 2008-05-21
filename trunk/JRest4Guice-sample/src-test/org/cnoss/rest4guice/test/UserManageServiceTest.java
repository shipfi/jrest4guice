package org.cnoss.rest4guice.test;

import java.util.List;

import junit.framework.Assert;

import org.jrest4guice.JRest4GuiceHelper;
import org.jrest4guice.core.guice.GuiceContext;
import org.jrest4guice.sample.entity.Role;
import org.jrest4guice.sample.entity.User;
import org.jrest4guice.sample.service.UserManageService;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 * 
 */
public class UserManageServiceTest {

	private static UserManageService service;

	@BeforeClass
	public static void setUp() throws Exception {
		JRest4GuiceHelper.useJRest("org.jrest4guice.sample.resources").useJPA()
				.useSecurity().init();

		service = GuiceContext.getInstance().getBean(UserManageService.class);
	}

	@Test
	public void testAuthUser() {
		Assert.assertTrue(service.authUser("cnoss", "123"));
	}

	@Test
	public void testGetUserRoles() {
		System.out.println("\nRoles");
		System.out.println("================================");

		List<Role> userRoles = service
				.getUserRoles("602881e417bb78010117bba509130001");
		for (Role role : userRoles)
			System.out.println(role.getName());
	}

	@Test
	public void testGetAllUsers() {
		System.out.println("\nUsers");
		System.out.println("================================");
		List<User> users = service.getAllUsers();
		for (User user : users)
			System.out.println(user.getName());
	}
}
