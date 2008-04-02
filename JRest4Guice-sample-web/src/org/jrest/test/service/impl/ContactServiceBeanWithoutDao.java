package org.jrest.test.service.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.jrest.core.transaction.annotations.Transactional;
import org.jrest.test.entity.Contact;

import com.google.inject.Inject;

@SuppressWarnings("unchecked")
public class ContactServiceBeanWithoutDao{
	@Inject
	private EntityManager em;

	@Transactional
	public String createContact(Contact contact) {
		if (contact == null)
			throw new RuntimeException("联系人的内容不能为空");

		if (this.em.createNamedQuery("byName").setParameter("name", contact.getName()).getResultList().size() > 0) {
			throw new RuntimeException("联系人的姓名相同，请重新输入");
		}

		this.em.persist(contact);
		return contact.getId();
	}

	@Transactional
	public void deleteContact(String contactId) {
		Contact contact = this.findContactById(contactId);
		if (contact == null)
			throw new RuntimeException("联系人不存在");

		this.em.remove(contact);
	}

	public Contact findContactById(String contactId) {
		Contact contact = this.em.find(Contact.class,contactId);
		return contact;
	}

	public List<Contact> listContacts(int first, int max)
			throws RuntimeException {
		return this.em.createNamedQuery("list").setFirstResult(first).setMaxResults(max).getResultList();
	}

	public List<Contact> listContactByDate(Object time)
			throws RuntimeException {
		return this.em.createNamedQuery("byDate").setParameter("changeDate", time).getResultList();
	}

	@Transactional
	public void updateContact(Contact contact) {
		if (contact == null)
			throw new RuntimeException("联系人的内容不能为空");

		this.em.merge(contact);
	}
}
