package org.cnoss.rest4guice.test;

import java.util.List;

import org.jrest4guice.client.JRestClient;
import org.jrest4guice.client.ModelMap;
import org.jrest4guice.client.Page;
import org.jrest4guice.rest.RemoteServiceDynamicProxy;
import org.jrest4guice.sample.contact.entity.Contact;
import org.jrest4guice.sample.contact.resources.ContactResource;
import org.jrest4guice.security.Role;

public class TestRemoteCall {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		//通过JRestClient直接访问rest资源
		JRestClient client = new JRestClient();
		ModelMap<String, Object> urlParam = new ModelMap<String, Object>();
		urlParam.put("pageIndex", "1");
		urlParam.put("pageSize", "5");
		Object result = client.callRemote("http://localhost/full/resource/contacts",
				"get", urlParam);

		Page<Contact> page = (Page<Contact>) result;
		List<Contact> contacts = page.getResult();
		for (Contact ct : contacts)
			System.out.println(ct.getName());

		System.out.println("==================================");
		List<Role> roles = (List<Role>) client.callRemote(
				"http://localhost/full/resource/security/cnoss/roles", "get", null);
		for (Role r : roles)
			System.out.println(r.getName());

		System.out.println("==================================");
		

		//测试通过远程代理方式调用远程服务
		RemoteServiceDynamicProxy proxy = new RemoteServiceDynamicProxy();
		ContactResource resource = proxy
				.createRemoteService(ContactResource.class);

		page = resource.listContacts(1, 2);
		contacts = page.getResult();
		for (Contact ct : contacts)
			System.out.println(ct.getName());
	}
}
