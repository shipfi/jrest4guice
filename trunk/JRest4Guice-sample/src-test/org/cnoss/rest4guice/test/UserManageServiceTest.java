package org.cnoss.rest4guice.test;

import static org.junit.Assert.fail;

import java.util.List;

import junit.framework.Assert;

import org.jrest4guice.core.guice.GuiceContext;
import org.jrest4guice.core.jpa.JpaGuiceModuleProvider;
import org.jrest4guice.core.transaction.TransactionGuiceModuleProvider;
import org.jrest4guice.core.util.MD5Util;
import org.jrest4guice.sample.entity.Role;
import org.jrest4guice.sample.entity.User;
import org.jrest4guice.sample.service.UserManageService;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserManageServiceTest {
	
	private static UserManageService service;
	
	@BeforeClass
	public static void setUp() throws Exception {
		System.out.println("构造");
		GuiceContext
		.getInstance()
		.addModuleProvider(new TransactionGuiceModuleProvider())
		.addModuleProvider(new JpaGuiceModuleProvider()).init();
		
		service = GuiceContext.getInstance().getBean(UserManageService.class);
	}

	@Test
	public void testAuthUser() {
		Assert.assertTrue(this.service.authUser("cnoss", "123"));
	}

	@Test
	public void testGetUserRoles() {
		System.out.println("\nRoles");
		System.out.println("================================");
		
		List<Role> userRoles = this.service.getUserRoles("602881e417bb78010117bba509130001");
		for(Role role :userRoles)
			System.out.println(role.getName());
	}

	@Test
	public void testGetAllUsers() {
		System.out.println("\nUsers");
		System.out.println("================================");
		List<User> users = this.service.getAllUsers();
		for(User user :users)
			System.out.println(user.getName());
	}
	
	public static void main(String[] args) {
		System.out.println(MD5Util.toMD5("123"));
	}
}
