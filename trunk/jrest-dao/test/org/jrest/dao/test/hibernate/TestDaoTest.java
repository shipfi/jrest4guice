package org.jrest.dao.test.hibernate;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jrest.dao.DynamicProxy;
import org.jrest.dao.test.entities.Author;
import org.jrest.dao.test.entities.Book;
import org.jrest.dao.test.entities.Category;
import org.jrest.dao.test.entities.PackingInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations = {"applicationContext.xml"})
public class TestDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

	private Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private DynamicProxy dynamicProxy;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Test
	public void testBookCRUD() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		TestDao dao = (TestDao) dynamicProxy.createDao(TestDao.class);
		
		Book book = new Book();
		book.setTitle("好书一本");
		book.setSummary("这是一本好书");
		book.setPrice(10.5f);
		dao.createBook(book);
		log.debug("Book ID:" + book.getId());
		
		Book reload = dao.loadBook(book.getId());
		Assert.assertEquals(book.getId(), reload.getId());
		Assert.assertEquals(0, reload.getAuthors().size());
		Assert.assertNull(reload.getPackingInfo());
		
		PackingInfo info = new PackingInfo("平装", "铜版纸", 99);
		book.setPackingInfo(info);
		List<Author> authors = new ArrayList<Author>();
		authors.add(new Author("gzYangfan"));
		book.setAuthors(authors);
		dao.updateBook(book);

		reload = dao.loadBook(book.getId());
		Assert.assertEquals(book.getId(), reload.getId());
		Assert.assertEquals(1, reload.getAuthors().size());
		Assert.assertNotNull(reload.getPackingInfo());
		
		dao.deleteBook(book);
		reload = dao.loadBook(book.getId());
		Assert.assertNull(reload);

		tx.commit();
		session.close();
	}

	@Test
	public void testCategoryCRUD() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		TestDao dao = (TestDao) dynamicProxy.createDao(TestDao.class);
		
		Category category = new Category();
		category.setName("科教");
		dao.createCategory(category);
		log.debug("Category ID:" + category.getId());
		
		Category reload = dao.loadCategory(category.getId());
		Assert.assertEquals(category.getId(), reload.getId());
		Assert.assertEquals("科教", reload.getName());
		
		category.setName("娱乐");
		dao.updateCategory(category);

		reload = dao.loadCategory(category.getId());
		Assert.assertEquals("娱乐", reload.getName());
		
		dao.deleteCategory(category);
		reload = dao.loadCategory(category.getId());
		Assert.assertNull(reload);

		tx.commit();
		session.close();
	}
}
