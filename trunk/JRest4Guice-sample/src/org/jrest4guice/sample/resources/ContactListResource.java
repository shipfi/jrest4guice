package org.jrest4guice.sample.resources;

import org.jrest4guice.annotation.Get;
import org.jrest4guice.annotation.ProduceMime;
import org.jrest4guice.annotation.Restful;
import org.jrest4guice.core.client.Page;
import org.jrest4guice.sample.entity.Contact;
import org.jrest4guice.sample.service.ContactService;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
@Restful(uri = "/contacts")
public class ContactListResource {
	@Inject
	private ContactService service;
	
	@Get
	@ProduceMime({"application/json"})
	public Page<Contact> getContact(int pageIndex, int pageSize) {
		return this.service.listContacts(pageIndex, pageSize);
	}
}
