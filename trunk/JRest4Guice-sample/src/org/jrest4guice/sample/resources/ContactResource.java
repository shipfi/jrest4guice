package org.jrest4guice.sample.resources;

import org.jrest4guice.client.Page;
import org.jrest4guice.rest.annotations.Delete;
import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.MimeType;
import org.jrest4guice.rest.annotations.ModelBean;
import org.jrest4guice.rest.annotations.Parameter;
import org.jrest4guice.rest.annotations.Path;
import org.jrest4guice.rest.annotations.Post;
import org.jrest4guice.rest.annotations.ProduceMime;
import org.jrest4guice.rest.annotations.Put;
import org.jrest4guice.rest.annotations.Remote;
import org.jrest4guice.sample.entity.Contact;
import org.jrest4guice.sample.service.ContactService;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 * 
 */
@Path( { "/contact", "/contacts/{contactId}" })
@Remote
public class ContactResource {
	@Inject
	private ContactService service;

	@Post
	public String createContact(@ModelBean Contact contact) {
		return this.service.createContact(contact);
	}

	@Put
	public void putContact(@ModelBean Contact contact) {
		this.service.updateContact(contact);
	}

	@Get
	@ProduceMime( {MimeType.MIME_OF_JSON,MimeType.MIME_OF_JAVABEAN})
	@Path("/contacts")
	public Page<Contact> listContacts(int pageIndex, int pageSize) {
		return this.service.listContacts(pageIndex, pageSize);
	}

	@Get
	public Contact getContact(@Parameter("contactId") String contactId) {
		return this.service.findContactById(contactId);
	}

	@Delete
	public void deleteContact(@Parameter("contactId") String contactId) {
		this.service.deleteContact(contactId);
	}
}
