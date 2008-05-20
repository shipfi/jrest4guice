package org.jrest4guice.sample.service;


import org.jrest4guice.core.persist.jpa.Page;
import org.jrest4guice.sample.entity.Contact;
import org.jrest4guice.sample.service.impl.ContactServiceBean;

import com.google.inject.ImplementedBy;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
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
