package org.jrest4guice.sample.contact.service;


import javax.annotation.security.RolesAllowed;

import org.jrest4guice.client.Page;
import org.jrest4guice.client.Pagination;
import org.jrest4guice.interceptor.annotations.Interceptor;
import org.jrest4guice.interceptor.annotations.Interceptors;
import org.jrest4guice.persistence.BaseEntityManager;
import org.jrest4guice.sample.contact.entity.Contact;
import org.jrest4guice.sample.contact.interceptor.ListContactsInterceptor;
import org.jrest4guice.sample.contact.interceptor.LogInterceptor;
import org.jrest4guice.sample.contact.interceptor.TestInterceptor;
import org.jrest4guice.transaction.annotations.Transactional;
import org.jrest4guice.transaction.annotations.TransactionalType;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
@Transactional//事务支持，缺省为TransactionalType.REQUIRED，可以在方法中覆写
@Interceptors({//自定义的拦截器（类级别的，作用于所有的方法，可以在方法中覆写）
	@Interceptor(TestInterceptor.class),
	@Interceptor(LogInterceptor.class)
})
public class ContactService{
	//注入实体管理器
	@Inject
	private BaseEntityManager<String, Contact> entityManager;

	public String createContact(Contact contact) {
		if (contact == null)
			throw new RuntimeException("联系人的内容不能为空");

		if (this.entityManager.loadByNamedQuery("byName", contact.getName()) != null) {
			throw new RuntimeException("联系人的姓名相同，请重新输入");
		}

		this.entityManager.create(contact);
		return contact.getId();
	}

	@RolesAllowed("admin")
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

	@Transactional(type=TransactionalType.READOLNY)
	public Contact findContactById(String contactId) {
		return this.entityManager.load(contactId);
	}

	@Transactional(type=TransactionalType.READOLNY)//覆盖类级别的事务类型为只读
	@Interceptor(ListContactsInterceptor.class)//覆盖类级别的拦截器
	public Page<Contact> listContacts(int pageIndex, int pageSize)
			throws RuntimeException {
		return this.entityManager.pageByNamedQuery("list",
				new Pagination(pageIndex, pageSize));
	}

	public void updateContact(Contact contact) {
		if (contact == null)
			throw new RuntimeException("联系人的内容不能为空");
		
		Contact tmpContact = this.entityManager.loadByNamedQuery("byName", contact.getName());
		if(tmpContact != null && !contact.getId().equals(tmpContact.getId()))
			throw new RuntimeException("联系人的姓名相同，请重新输入");

		this.entityManager.update(contact);
	}
}
