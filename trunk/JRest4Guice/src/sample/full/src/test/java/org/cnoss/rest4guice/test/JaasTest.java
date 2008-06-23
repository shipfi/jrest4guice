package org.cnoss.rest4guice.test;

import java.util.List;

import junit.framework.Assert;

import org.apache.commons.httpclient.HttpClient;
import org.jrest4guice.client.JRestClient;
import org.jrest4guice.client.ModelMap;
import org.jrest4guice.client.Page;
import org.jrest4guice.sample.contact.entity.Role;
import org.jrest4guice.security.SecurityHelper;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class JaasTest {
	private static JRestClient client;

	@BeforeClass
	public static void setUp() throws Exception {
		client = new JRestClient();
	}

	//@Test
	public void testWithoutLogin() {
		try {
			this.doTest(client);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void testWithLogin() {
		try {
			this.login(client.getHttpClient());
			this.doTest(client);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	public void login(HttpClient httpClient) throws Exception {
		new SecurityHelper().login(httpClient, "http://localhost/full",
				"/login.jsp", "cnoss", "123");
	}

	private void doTest(JRestClient client) throws Exception {
		ModelMap<String, Object> urlParam = new ModelMap<String, Object>();
		urlParam.put("pageIndex", "1");
		urlParam.put("pageSize", "5");
		Object result;
		result = client.callRemote(
				"http://localhost/full/resource/user/cnoss/roles", "get",
				urlParam);

		Page<Role> page = (Page<Role>) result;
		Assert.assertNotNull(page);
		List<Role> roles = page.getResult();

		Assert.assertTrue(roles.size() == 3);
		for (Role role : roles)
			System.out.println(role.getName());
	}
}
