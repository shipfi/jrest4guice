package org.jrest4guice.sample.resources;

import org.jrest4guice.annotation.Delete;
import org.jrest4guice.annotation.Get;
import org.jrest4guice.annotation.ModelBean;
import org.jrest4guice.annotation.Parameter;
import org.jrest4guice.annotation.Post;
import org.jrest4guice.annotation.ProduceMime;
import org.jrest4guice.annotation.Put;
import org.jrest4guice.annotation.Path;
import org.jrest4guice.core.client.Page;
import org.jrest4guice.sample.entity.Contact;
import org.jrest4guice.sample.service.ContactService;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 * 
 */
@Path( { "/contact", "/contact/{contactId}" })
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
	@ProduceMime( { "application/json" })
	@Path("/contacts")
	public Page<Contact> listContacts(int pageIndex, int pageSize) {
		return this.service.listContacts(pageIndex, pageSize);
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
