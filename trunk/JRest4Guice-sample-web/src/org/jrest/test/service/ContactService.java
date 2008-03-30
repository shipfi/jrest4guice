package org.jrest.test.service;

import java.util.List;

import org.jrest.test.entity.Contact;
import org.jrest.test.service.impl.ContactServiceBean;

import com.google.inject.ImplementedBy;

@ImplementedBy(ContactServiceBean.class)
public interface ContactService {
	public String createContact(Contact contact);
	
	public List<Contact> listContacts(int first,int max);
	
	public Contact findContactById(String contactId);
	
	public void updateContact(Contact contact);

	public void deleteContact(String contactId);
}
