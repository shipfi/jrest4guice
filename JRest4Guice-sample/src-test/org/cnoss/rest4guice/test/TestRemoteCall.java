package org.cnoss.rest4guice.test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jrest4guice.RemoteServiceDynamicProxy;
import org.jrest4guice.RequestProcessor;
import org.jrest4guice.annotations.Remote;
import org.jrest4guice.client.JRestClient;
import org.jrest4guice.core.client.Page;
import org.jrest4guice.core.security.Role;
import org.jrest4guice.core.util.ParameterNameDiscoverer;
import org.jrest4guice.sample.entity.Contact;
import org.jrest4guice.sample.resources.ContactResource;
import org.jrest4guice.sample.service.ContactService;

public class TestRemoteCall {
	public static void main(String[] args) throws Exception {
		JRestClient client = new JRestClient();
		Map<String, String> urlParam = new HashMap<String, String>();
		urlParam.put("pageIndex", "1");
		urlParam.put("pageSize", "5");
		Object result = client.callRemote("http://localhost/resource/contacts",
				"get", urlParam);

		Page<Contact> page = (Page<Contact>) result;
		List<Contact> contacts = page.getResult();
		for (Contact ct : contacts)
			System.out.println(ct.getName());

		System.out.println("==================================");
		urlParam.clear();
		urlParam.put("userName", "cnoss");
		urlParam.put("userPassword", "123");
		List<Role> roles = (List<Role>) client.callRemote(
				"http://localhost/resource/security/cnoss/roles", "get", null);
		for (Role r : roles)
			System.out.println(r.getName());

		System.out.println("==================================");
		RemoteServiceDynamicProxy proxy = new RemoteServiceDynamicProxy();
		ContactResource resource = proxy
				.createRemoteService(ContactResource.class);
		page = resource.listContacts(1, 2);
		contacts = page.getResult();
		for (Contact ct : contacts)
			System.out.println(ct.getName());
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = null;

		JRestClient client = new JRestClient();

		Class<?> clazz = proxy.getClass();
		Remote remote = clazz.getAnnotation(Remote.class);
		String name = remote.value();
		if (name == null || name.trim().equals(""))
			name = clazz.getName();

		int index = -1;
		Method[] methods = clazz.getMethods();
		for (Method m : methods) {
			index++;
			if (m.equals(method)) {
				break;
			}
		}

		Map<String, String> urlParam = null;
		String serviceUrl = "http://localhost/resource/"
				+ Remote.REMOTE_SERVICE_PREFIX + "?"
				+ Remote.REMOTE_SERVICE_NAME_KEY + "=" + name + "&"
				+ Remote.REMOTE_SERVICE_METHOD_INDEX_KEY + "=" + index;
		if (args != null && args.length > 0) {
			ParameterNameDiscoverer pnDiscoverer = new ParameterNameDiscoverer();
			String[] parameterNames = pnDiscoverer.getParameterNames(method);
			urlParam = new HashMap<String, String>();
			int i = 0;
			for (Object arg : args) {
				urlParam.put(parameterNames[i], arg.toString());
				i++;
			}
		}

		result = client.callRemote(serviceUrl, RequestProcessor.METHOD_OF_POST,
				urlParam);

		return result;
	}

}
