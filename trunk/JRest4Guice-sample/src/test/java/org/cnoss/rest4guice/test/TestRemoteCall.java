package org.cnoss.rest4guice.test;

import java.util.List;

import org.jrest4guice.client.Page;
import org.jrest4guice.rest.RemoteServiceDynamicProxy;
import org.jrest4guice.sample.contact.entity.Contact;
import org.jrest4guice.sample.contact.entity.User;
import org.jrest4guice.sample.contact.resources.ContactResource;

public class TestRemoteCall {
	public static void main(String[] args) throws Exception {
//		JRestClient client = new JRestClient();
//		ModelMap<String, Object> urlParam = new ModelMap<String, Object>();
//		urlParam.put("pageIndex", "1");
//		urlParam.put("pageSize", "5");
//		Object result = client.callRemote("http://localhost/resource/contacts",
//				"get", urlParam);

//		Page<Contact> page = (Page<Contact>) result;
//		List<Contact> contacts = page.getResult();
//		for (Contact ct : contacts)
//			System.out.println(ct.getName());

//		System.out.println("==================================");
//		urlParam.clear();
//		urlParam.put("userName", "cnoss");
//		urlParam.put("userPassword", "123");
//		List<Role> roles = (List<Role>) client.callRemote(
//				"http://localhost/resource/security/cnoss/roles", "get", null);
//		for (Role r : roles)
//			System.out.println(r.getName());
//
//		System.out.println("==================================");
		RemoteServiceDynamicProxy proxy = new RemoteServiceDynamicProxy();
		ContactResource resource = proxy
				.createRemoteService(ContactResource.class);
		
		Page<Contact> page = resource.listContacts(1, 2);
		List<Contact> contacts = page.getResult();
		for (Contact ct : contacts)
			System.out.println(ct.getName());
	}
}
