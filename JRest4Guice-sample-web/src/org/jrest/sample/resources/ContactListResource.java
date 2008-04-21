package org.jrest.sample.resources;

import java.util.List;

import org.jrest.rest.annotation.HttpMethod;
import org.jrest.rest.annotation.HttpMethodType;
import org.jrest.rest.annotation.ProduceMime;
import org.jrest.rest.annotation.Restful;
import org.jrest.sample.service.ContactService;

import com.google.inject.Inject;

@Restful(uri = "/contacts")
public class ContactListResource {
	@Inject
	private ContactService service;

	@HttpMethod(type = HttpMethodType.GET)
	@ProduceMime({"application/json"})
	public List getContact(int first, int max) {
		return this.service.listContacts(first, max);
	}
}
