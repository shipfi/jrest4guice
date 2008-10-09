package org.jrest4guice.sample.contact.test;

import java.util.List;

import junit.framework.Assert;

import org.jrest4guice.client.Page;
import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.guice.PersistenceGuiceContext;
import org.jrest4guice.sample.contact.entity.Role;
import org.jrest4guice.sample.contact.entity.User;
import org.jrest4guice.sample.contact.service.UserManageService;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
public class UserManageServiceTest {

	private static UserManageService service;

	@BeforeClass
	public static void setUp() throws Exception {
		//初始化JRest4Guice
		PersistenceGuiceContext.getInstance().useJPA().init();
		//获取服务
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
				.getUserRoles("cnoss");
		Assert.assertTrue(userRoles.size()==2);
		for (Role role : userRoles)
			System.out.println(role.getName());
	}

	@Test
	public void testGetAllUsers() {
		System.out.println("\nUsers");
		System.out.println("================================");
		Page<User> page = service.getAllUsers(1, 100);
		List<User> users = page.getResult();
		Assert.assertTrue(users.size()==2);
		for (User user : users)
			System.out.println(user.getName());
	}
}
