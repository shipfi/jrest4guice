package org.cnoss.rest.sample.resources;

import org.cnoss.core.persist.jpa.Page;
import org.cnoss.rest.annotation.Get;
import org.cnoss.rest.annotation.ProduceMime;
import org.cnoss.rest.annotation.Restful;
import org.cnoss.rest.sample.entity.Contact;
import org.cnoss.rest.sample.service.ContactService;

import com.google.inject.Inject;

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
