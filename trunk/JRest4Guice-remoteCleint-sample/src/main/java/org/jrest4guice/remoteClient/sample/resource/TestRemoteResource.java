package org.jrest4guice.remoteClient.sample.resource;

import org.jrest4guice.client.Page;
import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.MimeType;
import org.jrest4guice.rest.annotations.Path;
import org.jrest4guice.rest.annotations.ProduceMime;
import org.jrest4guice.rest.annotations.RemoteService;
import org.jrest4guice.sample.entity.Contact;
import org.jrest4guice.sample.resources.ContactResource;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ：86895156)</a>
 *
 */
@Path( { "/testCallRemote"})
public class TestRemoteResource {
	@Inject
	@RemoteService
	private ContactResource service;

	@Get
	@ProduceMime( {MimeType.MIME_OF_JSON,MimeType.MIME_OF_JAVABEAN})
	public Page<Contact> listContacts(int pageIndex, int pageSize) {
		return this.service.listContacts(pageIndex, pageSize);
	}
}