package org.jrest.sample.resources;

import org.jrest.core.persist.jpa.Page;
import org.jrest.rest.annotation.Get;
import org.jrest.rest.annotation.ProduceMime;
import org.jrest.rest.annotation.Restful;
import org.jrest.sample.entity.Contact;
import org.jrest.sample.service.ContactService;

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
