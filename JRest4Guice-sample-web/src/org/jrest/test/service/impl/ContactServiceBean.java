package org.jrest.test.service.impl;

import java.rmi.RemoteException;
import java.util.List;

import javax.persistence.EntityManager;

import org.jpa4guice.annotation.Transactional;
import org.jrest.test.entity.Contact;
import org.jrest.test.service.ContactService;

import com.google.inject.Inject;

public class ContactServiceBean implements ContactService {
	@Inject
	private EntityManager em;

	@Override
	@Transactional
	public String createContact(Contact contact) throws RemoteException {
		if (contact == null)
			throw new RemoteException("联系人的内容不能为空");
		
		if(this.em.createNamedQuery("byName").setParameter("name", contact.getName()).getResultList().size()>0){
			throw new RemoteException("联系人的姓名相同，请重新输入");
		}

		this.em.persist(contact);
		return contact.getId();
	}

	@Override
	@Transactional
	public void deleteContact(String contactId) throws RemoteException {
		Contact contact = this.findContactById(contactId);
		if (contact == null)
			throw new RemoteException("联系人不存在");

		this.em.remove(contact);
	}

	@Override
	@Transactional
	public Contact findContactById(String contactId) throws RemoteException {
		return this.em.find(Contact.class, contactId);
	}

	@Override
	@Transactional
	public List<Contact> listContacts(int first, int max)
			throws RemoteException {
		return this.em.createNamedQuery("list").setFirstResult(first)
				.setMaxResults(max).getResultList();
	}

	@Override
	@Transactional
	public void updateContact(Contact contact) throws RemoteException {
		if (contact == null)
			throw new RemoteException("联系人的内容不能为空");

		this.em.merge(contact);
	}
}
