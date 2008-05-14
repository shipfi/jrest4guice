package org.jrest.sample.service.impl;


import org.jrest.core.persist.jpa.Page;
import org.jrest.core.transaction.annotations.Transactional;
import org.jrest.core.transaction.annotations.TransactionalType;
import org.jrest.sample.dao.ContactDao;
import org.jrest.sample.entity.Contact;
import org.jrest.sample.service.ContactService;

import com.google.inject.Inject;

@SuppressWarnings("unchecked")
public class ContactServiceBeanWithDao implements ContactService {
	@Inject
	private ContactDao dao;// 注入联系人DAO

	@Transactional
	public String createContact(Contact contact) {
		if (contact == null)
			throw new RuntimeException("联系人的内容不能为空");

		if (this.dao.findContactByName(contact.getName())!= null) {
			throw new RuntimeException("联系人的姓名相同，请重新输入");
		}

		this.dao.createContact(contact);
		return contact.getId();
	}

	@Transactional
	public void deleteContact(String contactId) {
		String[] ids = contactId.split(",");
		Contact contact;
		for(String id:ids){
			contact = this.findContactById(id);
			if (contact == null)
				throw new RuntimeException("联系人不存在");
			this.dao.deleteContact(contact);
		}
	}

	@Transactional(type=TransactionalType.READOLNY)
	public Contact findContactById(String contactId) {
		Contact contact = this.dao.findContactById(contactId);
		return contact;
	}

	@Transactional(type=TransactionalType.READOLNY)
	public Page<Contact> listContacts(int pageIndex, int pageSize)
			throws RuntimeException {
		Page<Contact> pages = new Page<Contact>(1,100,1,this.dao.listContacts(pageIndex, pageSize));
		return pages;
	}

	@Transactional
	public void updateContact(Contact contact) {
		if (contact == null)
			throw new RuntimeException("联系人的内容不能为空");

		this.dao.updateContact(contact);
	}
}
