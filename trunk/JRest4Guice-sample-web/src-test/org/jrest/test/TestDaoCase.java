package org.jrest.test;

import java.util.List;

import junit.framework.Assert;

import org.jrest.core.guice.GuiceContext;
import org.jrest.core.persist.jpa.JpaGuiceModuleProvider;
import org.jrest.core.transaction.TransactionGuiceModuleProvider;
import org.jrest.dao.DaoModuleProvider;
import org.jrest.sample.dao.ContactDao;
import org.jrest.sample.entity.Contact;
import org.jrest.sample.service.impl.ContactServiceBeanWithoutDao;
import org.junit.Test;

public class TestDaoCase {
	public TestDaoCase() {
	}
	
	@Test
	public void testContactServiceWithoutDao() {
		System.out.println("testContactServiceWithoutDao");
		System.out.println("====================================================");
		GuiceContext guice = GuiceContext.getInstance();
		// 初始化Guice上下文
		guice.addModuleProvider(new TransactionGuiceModuleProvider()).addModuleProvider(new JpaGuiceModuleProvider())
		        .init();
		
		ContactServiceBeanWithoutDao bean = guice.getBean(ContactServiceBeanWithoutDao.class);
		Assert.assertNotNull(bean);
		
		List<Contact> contacts = bean.listContacts(1, 100);
		Assert.assertTrue(contacts.size() > 0);
		
		debugContacts(contacts);
	}
	
	// @Test
	public void testContactDao() {
		System.out.println("testContactDao");
		System.out.println("====================================================");
		GuiceContext guice = GuiceContext.getInstance();
		// 初始化Guice上下文
		guice.addModuleProvider(new TransactionGuiceModuleProvider()).addModuleProvider(new JpaGuiceModuleProvider())
		        .addModuleProvider(new DaoModuleProvider("org.jrest.sample.dao"));
		
		// 从Guice上下文中获取联系人DAO实例
		ContactDao dao = guice.getBean(ContactDao.class);
		
		Assert.assertNotNull(dao);
		List<Contact> contacts = null;
		
		Contact contact = dao.findContactByName("王二小");
		Assert.assertNotNull(contact);
		
		dao.deleteContact(contacts.get(0));
		
		contacts = dao.listContacts(1, 100);
		Assert.assertTrue(contacts.size() == 18);
		
		debugContacts(contacts);
	}
	
	private void debugContacts(List<Contact> contacts) {
		for (Contact contact : contacts)
			System.out.println("我是" + contact.getName() + "，现住在" + contact.getAddress());
	}
}
