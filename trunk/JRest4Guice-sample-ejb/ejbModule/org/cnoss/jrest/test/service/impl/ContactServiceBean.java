package org.cnoss.jrest.test.service.impl;

import java.rmi.RemoteException;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.cnoss.jrest.test.service.ContactService;
import org.cnoss.jrest.test.service.entity.Contact;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.logging.Logger;

@Stateless
@Remote(ContactService.class)
@Local(ContactService.class)
@RemoteBinding(jndiBinding = "test/ContactService")
public class ContactServiceBean implements ContactService {
	static Logger log = Logger.getLogger(ContactServiceBean.class);

	@Resource
	SessionContext sessionContext;

	@PersistenceContext(unitName = "JRest")
	private EntityManager em;

	@Override
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
	public void deleteContact(String contactId) throws RemoteException {
		Contact contact = this.findContactById(contactId);
		if (contact == null)
			throw new RemoteException("联系人不存在");

		this.em.remove(contact);
	}

	@Override
	public Contact findContactById(String contactId) throws RemoteException {
		return this.em.find(Contact.class, contactId);
	}

	@Override
	public List<Contact> listContacts(int first, int max)
			throws RemoteException {
		return this.em.createNamedQuery("list").setFirstResult(first)
				.setMaxResults(max).getResultList();
	}

	@Override
	public void updateContact(Contact contact) throws RemoteException {
		if (contact == null)
			throw new RemoteException("联系人的内容不能为空");

		this.em.merge(contact);
	}
}
