package org.jrest4guice.remoteClient.sample.resource;

import org.jrest4guice.annotations.Get;
import org.jrest4guice.annotations.MimeType;
import org.jrest4guice.annotations.Path;
import org.jrest4guice.annotations.ProduceMime;
import org.jrest4guice.annotations.RemoteService;
import org.jrest4guice.core.client.Page;
import org.jrest4guice.sample.entity.Contact;
import org.jrest4guice.sample.resources.ContactResource;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
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