package org.jrest.test.service;

import java.util.List;

import org.jrest.test.entity.Contact;
import org.jrest.test.service.impl.ContactServiceBeanWithDao;

import com.google.inject.ImplementedBy;

@ImplementedBy(ContactServiceBeanWithDao.class)
public interface ContactService {
	public String createContact(Contact contact);
	
	public List<Contact> listContacts(int first,int max);
	
	public List<Contact> listContactByDate(Object time);

	public Contact findContactById(String contactId);
	
	public void updateContact(Contact contact);

	public void deleteContact(String contactId);
}
