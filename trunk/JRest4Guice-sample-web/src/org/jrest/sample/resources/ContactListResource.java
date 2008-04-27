package org.jrest.sample.resources;

import java.util.List;

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
	public List<Contact> getContact(int first, int max) {
		return this.service.listContacts(first, max);
	}
}
