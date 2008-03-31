package org.jrest.test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import junit.framework.TestCase;

import org.jrest.core.guice.GuiceContext;
import org.jrest.core.util.ClassScanListener;
import org.jrest.dao.jpa.JpaDaoScanListener;
import org.jrest.test.dao.ContactDao;
import org.jrest.test.entity.Contact;
import org.junit.Test;

public class TestDaoCase extends TestCase {
	@Test
	public void testDao() {
		GuiceContext
				.getInstance()
				.init(
						null,
						new HashSet<String>(Arrays
								.asList(new String[] { "org.jrest.test" })),
						Arrays
								.asList(new ClassScanListener[] { new JpaDaoScanListener() }));
		
		ContactDao dao = GuiceContext.getInstance().getBean(ContactDao.class);
		assertNotNull(dao);
		List<Contact> contacts = dao.listContacts(1, 100);
		assertTrue(contacts.size()>0);
		
		for(Contact contact :contacts)
			System.out.println("我是"+contact.getName()+"，现住在"+contact.getAddress());
	}
}
