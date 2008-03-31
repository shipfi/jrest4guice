package org.jrest.dao.test.hibernate;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jrest.dao.DynamicProxy;
import org.jrest.dao.test.entities.Author;
import org.jrest.dao.test.entities.Book;
import org.jrest.dao.test.entities.PackingInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations = {"applicationContext.xml"})
public class FindTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private DynamicProxy dynamicProxy;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Test
	public void testFind() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		TestDao dao = (TestDao) dynamicProxy.createDao(TestDao.class);

		List<Book> books = dao.allBooks();
		Assert.assertEquals(5, books.size());
		books = dao.findBooksPriceMoreThan(30f);
		Assert.assertEquals(2, books.size());
		books = dao.findBooksPriceMoreThan(20f, 2, 1);
		Assert.assertEquals(1, books.size());
		books = dao.lengthMoreThan(10);
		Assert.assertEquals(4, books.size());
		
		tx.commit();
		session.close();
	}
	
	private Book getNewBook(String title, float price, int length) {
		PackingInfo info = new PackingInfo("平装", "铜版纸", length);
		List<Author> authors = new ArrayList<Author>();
		authors.add(new Author("gzYangfan"));
		
		Book book = new Book();
		book.setTitle(title);
		book.setSummary("java");
		book.setPrice(price);
		book.setPackingInfo(info);
		book.setAuthors(authors);
		
		return book;
	}

	@Before
	public void setup() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		TestDao dao = (TestDao) dynamicProxy.createDao(TestDao.class);

		dao.createBook(getNewBook("Book 1", 10f, 10));
		dao.createBook(getNewBook("Book 2", 20f, 20));
		dao.createBook(getNewBook("Book 3", 30f, 30));
		dao.createBook(getNewBook("Book 4", 40f, 40));
		dao.createBook(getNewBook("Book 5", 50f, 50));
		
		tx.commit();
		session.close();
	}

	@After
	public void tearDown() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		TestDao dao = (TestDao) dynamicProxy.createDao(TestDao.class);

		List<Book> books = dao.allBooks();
		for (Book book : books) {
			dao.deleteBook(book);
		}

		tx.commit();
		session.close();
	}
}
