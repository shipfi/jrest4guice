package org.cnoss.rest.sample.resources;

import org.cnoss.rest.annotation.Delete;
import org.cnoss.rest.annotation.Get;
import org.cnoss.rest.annotation.ModelBean;
import org.cnoss.rest.annotation.Parameter;
import org.cnoss.rest.annotation.Post;
import org.cnoss.rest.annotation.Put;
import org.cnoss.rest.annotation.Restful;
import org.cnoss.rest.sample.entity.Contact;
import org.cnoss.rest.sample.service.ContactService;

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
