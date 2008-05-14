package org.jrest.sample.service;


import org.jrest.core.persist.jpa.Page;
import org.jrest.sample.entity.Contact;
import org.jrest.sample.service.impl.ContactServiceBeanWithBaseEntityManager;

import com.google.inject.ImplementedBy;

//@ImplementedBy(ContactServiceBeanWithDao.class)
@ImplementedBy(ContactServiceBeanWithBaseEntityManager.class)
public interface ContactService {
	public String createContact(Contact contact);
	
	public Page<Contact> listContacts(int pageIndex,int pageSize);
	
	public Contact findContactById(String contactId);
	
	public void updateContact(Contact contact);

	public void deleteContact(String contactId);
}
