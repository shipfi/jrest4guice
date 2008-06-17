package org.jrest4guice.sample.contact.service;


import org.jrest4guice.client.Page;
import org.jrest4guice.sample.contact.entity.Contact;
import org.jrest4guice.sample.contact.service.impl.ContactServiceBean;

import com.google.inject.ImplementedBy;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
@ImplementedBy(ContactServiceBean.class)
public interface ContactService {
	public String createContact(Contact contact);
	
	public Page<Contact> listContacts(int pageIndex,int pageSize);
	
	public Contact findContactById(String contactId);
	
	public void updateContact(Contact contact);

	public void deleteContact(String contactId);
}
