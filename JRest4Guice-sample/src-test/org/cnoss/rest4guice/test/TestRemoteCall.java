package org.cnoss.rest4guice.test;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.jrest4guice.annotations.MimeType;
import org.jrest4guice.core.client.Page;
import org.jrest4guice.core.security.Role;
import org.jrest4guice.sample.entity.Contact;

public class TestRemoteCall {
	public static void main(String[] args) throws Exception {
		TestRemoteCall call = new TestRemoteCall();
		Map<String, String> urlParam = new HashMap<String, String>();
		urlParam.put("pageIndex", "1");
		urlParam.put("pageSize", "5");
		Object result = call.doGet("http://localhost/resource/contacts",
				urlParam);
		Page<Contact> page = (Page<Contact>) result;
		List<Contact> contacts = page.getResult();
		for (Contact ct : contacts)
			System.out.println(ct.getName());

		urlParam.clear();
		urlParam.put("userName", "cnoss");
		urlParam.put("userPassword", "123");
		List<Role> roles = (List<Role>) call.doGet(
				"http://localhost/resource/security/cnoss/roles", null);
		for (Role r : roles)
			System.out.println(r.getName());
	}

	public Object doGet(String url, Map<String, String> urlParam)
			throws Exception {
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		Object responseBody = null;
		try {
			method.addRequestHeader("accept", MimeType.MIME_OF_JAVABEAN);
			if (urlParam != null) {
				Set<String> keySet = urlParam.keySet();
				List<String> params = new ArrayList<String>();
				for (String key : keySet) {
					params.add(key + "=" + urlParam.get(key));
				}
				String queryString = StringUtils.join(params.toArray(), "&");
				method.setQueryString(queryString);
			}

			// Execute the method.
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + method.getStatusLine());
			}

			ObjectInputStream obj_in = new ObjectInputStream(method
					.getResponseBodyAsStream());
			responseBody = obj_in.readObject();

		} catch (Exception e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}

		return responseBody;
	}

}
