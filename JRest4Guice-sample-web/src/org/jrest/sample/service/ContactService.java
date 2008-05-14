package org.jrest.sample.service;

import java.util.List;

import org.jrest.sample.entity.Contact;
import org.jrest.sample.service.impl.ContactServiceBeanWithBaseEntityManager;

import com.google.inject.ImplementedBy;

//@ImplementedBy(ContactServiceBeanWithDao.class)
@ImplementedBy(ContactServiceBeanWithBaseEntityManager.class)
public interface ContactService {
	public String createContact(Contact contact);
	
	public List<Contact> listContacts(int first,int max);
	
	public Contact findContactById(String contactId);
	
	public void updateContact(Contact contact);

	public void deleteContact(String contactId);
}
