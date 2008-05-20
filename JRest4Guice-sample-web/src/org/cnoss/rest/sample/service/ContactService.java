package org.cnoss.rest.sample.service;


import org.cnoss.core.persist.jpa.Page;
import org.cnoss.rest.sample.entity.Contact;
import org.cnoss.rest.sample.service.impl.ContactServiceBean;

import com.google.inject.ImplementedBy;

@ImplementedBy(ContactServiceBean.class)
public interface ContactService {
	public String createContact(Contact contact);
	
	public Page<Contact> listContacts(int pageIndex,int pageSize);
	
	public Contact findContactById(String contactId);
	
	public void updateContact(Contact contact);

	public void deleteContact(String contactId);
}
