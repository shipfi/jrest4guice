package org.jrest.sample.resources;

import org.jrest.rest.annotation.HttpMethod;
import org.jrest.rest.annotation.HttpMethodType;
import org.jrest.rest.annotation.ModelBean;
import org.jrest.rest.annotation.RequestParameter;
import org.jrest.rest.annotation.Restful;
import org.jrest.sample.entity.Contact;
import org.jrest.sample.service.ContactService;

import com.google.inject.Inject;

@Restful(uri = { "/contact", "/contact/{contactId}" })
public class ContactController {
	@Inject
	private ContactService service;

	@HttpMethod(type = HttpMethodType.POST)
	public String createContact(@ModelBean
	Contact contact) {
		return this.service.createContact(contact);
	}

	@HttpMethod
	public void putContact(@RequestParameter("contactId")
	String contactId, @ModelBean
	Contact contact) {
		if (contactId == null)
			throw new RuntimeException("没有指定对应的联系人标识符");
		this.service.updateContact(contact);
	}

	@HttpMethod
	public Contact getContact(@RequestParameter("contactId")
	String contactId) {
		return this.service.findContactById(contactId);
	}

	@HttpMethod
	public void deleteContact(@RequestParameter("contactId")
	String contactId) {
		this.service.deleteContact(contactId);
	}
}
