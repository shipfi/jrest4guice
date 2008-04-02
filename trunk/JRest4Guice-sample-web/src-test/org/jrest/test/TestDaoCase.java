package org.jrest.test;


import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.jrest.core.guice.GuiceContext;
import org.jrest.core.util.ClassScanListener;
import org.jrest.dao.DaoScanListener;
import org.jrest.test.dao.ContactDao;
import org.jrest.test.entity.Contact;
import org.jrest.test.service.impl.ContactServiceBeanWithoutDao;
import org.junit.Test;

public class TestDaoCase {
	public TestDaoCase(){
	}
	
	//@Test
	public void testContactServiceWithoutDao() {
		System.out.println("testContactServiceWithoutDao");
		System.out.println("====================================================");
		GuiceContext guice = GuiceContext.getInstance();
		// 初始化Guice上下文
		guice.useJPA().init(Arrays.asList(new String[] { "org.jrest.test" }), Arrays
				.asList(new ClassScanListener[] { new DaoScanListener() }));
		
		ContactServiceBeanWithoutDao bean = guice.getBean(ContactServiceBeanWithoutDao.class);
		Assert.assertNotNull(bean);

		List<Contact> contacts = bean.listContacts(1, 100);
		Assert.assertTrue(contacts.size() > 0);

		debugContacts(contacts);
	}

	@Test
	public void testContactDao() {
		System.out.println("testContactDao");
		System.out.println("====================================================");
		GuiceContext guice = GuiceContext.getInstance();
		// 初始化Guice上下文
		guice.useJPA().useDAO().init(Arrays.asList(new String[] { "org.jrest.test" }), Arrays
				.asList(new ClassScanListener[] { new DaoScanListener() }));
		
		// 从Guice上下文中获取联系人DAO实例
		ContactDao dao = guice.getBean(ContactDao.class);
		
		Assert.assertNotNull(dao);
		List<Contact> contacts = null;
		
		contacts = dao.findContactByName("王二小");
		Assert.assertTrue(contacts.size() == 1);
		
		dao.deleteContact(contacts.get(0));
		
		contacts = dao.listContacts(1, 100);
		Assert.assertTrue(contacts.size() == 18);

//		debugContacts(contacts);
	}

	private void debugContacts(List<Contact> contacts) {
		for (Contact contact : contacts)
			System.out.println("我是" + contact.getName() + "，现住在"
					+ contact.getAddress());
	}
}
