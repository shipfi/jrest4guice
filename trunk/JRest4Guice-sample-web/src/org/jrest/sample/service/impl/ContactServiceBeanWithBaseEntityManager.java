package org.jrest.sample.service.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.jrest.core.persist.jpa.BaseEntityManager;
import org.jrest.core.persist.jpa.Pagination;
import org.jrest.core.transaction.annotations.Transactional;
import org.jrest.sample.entity.Contact;
import org.jrest.sample.service.ContactService;

import com.google.inject.Inject;

@SuppressWarnings( { "unchecked", "unused" })
public class ContactServiceBeanWithBaseEntityManager implements ContactService {
	private BaseEntityManager<String, Contact> entityManager;

	@Inject
	private void init(EntityManager em) {
		this.entityManager = new BaseEntityManager<String, Contact>(
				Contact.class, em);
	}

	@Transactional
	public String createContact(Contact contact) {
		if (contact == null)
			throw new RuntimeException("联系人的内容不能为空");

		if (this.entityManager.loadByNamedQuery("byName", contact.getName()) != null) {
			throw new RuntimeException("联系人的姓名相同，请重新输入");
		}

		this.entityManager.create(contact);
		return contact.getId();
	}

	@Transactional
	public void deleteContact(String contactId) {
		String[] ids = contactId.split(",");
		Contact contact;
		for (String id : ids) {
			contact = this.findContactById(id);
			if (contact == null)
				throw new RuntimeException("联系人不存在");
			this.entityManager.delete(contact);
		}
	}

	@Transactional
	public Contact findContactById(String contactId) {
		return this.entityManager.load(contactId);
	}

	@Transactional
	public List<Contact> listContacts(int first, int max)
			throws RuntimeException {
		return this.entityManager.pageByNamedQuery("list",
				new Pagination(1, 100)).getResult();
	}

	@Transactional
	public void updateContact(Contact contact) {
		if (contact == null)
			throw new RuntimeException("联系人的内容不能为空");

		this.entityManager.update(contact);
	}
}
