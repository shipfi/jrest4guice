package org.jrest4guice.remoteClient.sample;

import java.util.List;

import org.jrest4guice.client.Page;
import org.jrest4guice.rest.RemoteServiceDynamicProxy;
import org.jrest4guice.sample.contact.entity.Contact;
import org.jrest4guice.sample.contact.resources.ContactResource;

public class TestCallRemote {
	public static void main(String[] args) {
		
		int times = 1000;
		int pageSize = 20;
		
		long start = System.currentTimeMillis();
		RemoteServiceDynamicProxy proxy = new RemoteServiceDynamicProxy();
		ContactResource resource = proxy
				.createRemoteService(ContactResource.class);
		
		Page<Contact> page = null;
		for(int i=0;i<times;i++){
			page = resource.listContacts(1, pageSize);
		}
		debug(page);
		
		long end = System.currentTimeMillis();

		System.out.println("\n运行 "+times+" 次，每次取 "+pageSize+" 条记录，总耗时： "+(end-start));
	}

	private static void debug(Page<Contact> page) {
		System.out.println();
		List<Contact> contacts = page.getResult();
		for (Contact ct : contacts)
			System.out.print(ct.getName()+",");
	}
}
