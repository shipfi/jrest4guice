package org.cnoss.rest4guice.test;

import java.util.List;

import junit.framework.Assert;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jrest4guice.client.JRestClient;
import org.jrest4guice.client.ModelMap;
import org.jrest4guice.client.Page;
import org.jrest4guice.sample.contact.entity.Role;
import org.jrest4guice.security.exception.LoginErrorException;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class JaasTest {
	static final String WEB_SITE = "localhost";
	static final int PORT = 80;

	private static JRestClient client;

	@BeforeClass
	public static void setUp() throws Exception {
		client = new JRestClient();
	}

	@Test
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

	private void login(HttpClient httpClient) throws Exception {
		Exception exception = null;
		
		GetMethod authget = null;
		PostMethod authpost = null;
		GetMethod redirect = null;
		try {
			httpClient.getHostConfiguration().setHost(WEB_SITE, PORT, "http");
			httpClient.getParams().setCookiePolicy(
					CookiePolicy.BROWSER_COMPATIBILITY);

			authget = new GetMethod("/full/login.jsp");

			httpClient.executeMethod(authget);
			int statuscode = authget.getStatusCode();

			authpost = new PostMethod("/full/j_security_check");
			NameValuePair userid = new NameValuePair("j_username", "cnoss");
			NameValuePair password = new NameValuePair("j_password", "123");
			authpost.setRequestBody(new NameValuePair[] { userid, password });

			httpClient.executeMethod(authpost);
			statuscode = authpost.getStatusCode();
			if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY)
					|| (statuscode == HttpStatus.SC_MOVED_PERMANENTLY)
					|| (statuscode == HttpStatus.SC_SEE_OTHER)
					|| (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
				Header header = authpost.getResponseHeader("location");
				if (header != null) {
					String newuri = header.getValue();
					if ((newuri == null) || (newuri.equals(""))) {
						newuri = "/";
					}
					redirect = new GetMethod(newuri);

					httpClient.executeMethod(redirect);
					redirect.releaseConnection();
				}
			}else{
				exception = new LoginErrorException("用户名或者密码错误");
			}
		} catch (Exception e) {
			exception = e;
		} finally {
			if(authget != null)
				authget.releaseConnection();
			if(authpost != null)
				authpost.releaseConnection();
			if(redirect != null)
				redirect.releaseConnection();
		}
		
		if(exception != null)
			throw exception;
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
