package org.jrest.sample.service;

import java.util.List;

import org.jrest.sample.entity.Contact;
import org.jrest.sample.service.impl.ContactServiceBeanWithoutDao;

import com.google.inject.ImplementedBy;

//@ImplementedBy(ContactServiceBeanWithDao.class)
@ImplementedBy(ContactServiceBeanWithoutDao.class)
public interface ContactService {
	public String createContact(Contact contact);
	
	public List<Contact> listContacts(int first,int max);
	
	public List<Contact> listContactByDate(Object time);

	public Contact findContactById(String contactId);
	
	public void updateContact(Contact contact);

	public void deleteContact(String contactId);
}
