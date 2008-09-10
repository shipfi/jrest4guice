package org.jrest4guice.sample.remoteClient.resource;

import org.jrest4guice.client.Page;
import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.Path;
import org.jrest4guice.rest.annotations.RemoteReference;
import org.jrest4guice.sample.contact.entity.Contact;
import org.jrest4guice.sample.contact.resource.ContactResource;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
@Path( { "/testCallRemote"})
public class TestRemoteResource {
	@Inject
	@RemoteReference//注入远程资源对象
	private ContactResource service;

	@Get
	public Page<Contact> listContacts(int page, int size) {
		return this.service.listContacts(page, size);
	}

	/**
	 * 测试远程调用的速度
	 * @param times
	 * @param pageSize
	 * @return
	 */
	@Get
	@Path("/test")
	public TestResult test(int times, int pageSize) {
		long start = System.currentTimeMillis();
		Page<Contact> page = null;
		for(int i=0;i<times;i++)
			page = this.service.listContacts(1, pageSize);
		long end = System.currentTimeMillis();
		
		return new TestResult(end-start,page);
	}
}