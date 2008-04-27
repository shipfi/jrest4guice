package org.jrest.sample.resources;

import org.jrest.rest.annotation.Delete;
import org.jrest.rest.annotation.Get;
import org.jrest.rest.annotation.ModelBean;
import org.jrest.rest.annotation.Post;
import org.jrest.rest.annotation.Put;
import org.jrest.rest.annotation.Parameter;
import org.jrest.rest.annotation.Restful;
import org.jrest.sample.entity.Contact;
import org.jrest.sample.service.ContactService;

import com.google.inject.Inject;

@Restful(uri = { "/contact", "/contact/{contactId}" })
public class ContactResource {
	@Inject
	private ContactService service;

	@Post
	public String createContact(@ModelBean
	Contact contact) {
		return this.service.createContact(contact);
	}

	@Put
	public void putContact(@Parameter("contactId")
	String contactId, @ModelBean
	Contact contact) {
		if (contactId == null)
			throw new RuntimeException("没有指定对应的联系人标识符");
		this.service.updateContact(contact);
	}

	@Get
	public Contact getContact(@Parameter("contactId")
	String contactId) {
		return this.service.findContactById(contactId);
	}

	@Delete
	public void deleteContact(@Parameter("contactId")
	String contactId) {
		this.service.deleteContact(contactId);
	}
}
