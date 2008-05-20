package org.jrest.dao.test.jpa;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.cnoss.core.guice.GuiceContext;
import org.cnoss.core.persist.jpa.JpaGuiceModuleProvider;
import org.jrest.dao.DaoModuleProvider;
import org.jrest.dao.jpa.JpaContextProvider;
import org.jrest.dao.test.entities.Author;
import org.jrest.dao.test.entities.Book;
import org.jrest.dao.test.entities.BookModel;
import org.jrest.dao.test.entities.PackingInfo;
import org.junit.BeforeClass;
import org.junit.Test;

public class RetrieveDaoTest {
	
	private static RetrieveDao dao;
	private static BookDao bookDao;
	private static String pk;
	
	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		GuiceContext context = GuiceContext.getInstance();
		context.addModuleProvider(
		        new JpaGuiceModuleProvider(),
		        (new DaoModuleProvider("org.jrest.dao.test.jpa", "org.jrest.dao.jpa"))
		                .addActionContextProviders(new JpaContextProvider())).init();
		bookDao = context.getBean(BookDao.class);
		
		Book b0 = getNewBook("Book 0", 10f, 10);
		Book b1 = getNewBook("Book 1", 10f, 20);
		Book b2 = getNewBook("Book 2", 10f, 30);
		Book b3 = getNewBook("Book 3", 10f, 40);
		Book b4 = getNewBook("Book 4", 10f, 50);
		Book b5 = getNewBook("Book 5", 20f, 60);
		Book b6 = getNewBook("Book 6", 20f, 70);
		Book b7 = getNewBook("Book 7", 20f, 80);
		Book b8 = getNewBook("Book 8", 20f, 90);
		Book b9 = getNewBook("Book 9", 20f, 100);
		bookDao.create(b0, b1, b2, b3, b4, b5, b6, b7, b8, b9);
		
		pk = b0.getId();
		Assert.assertNotNull(b0.getId());
		Assert.assertNotNull(b1.getId());
		Assert.assertNotNull(b2.getId());
		Assert.assertNotNull(b3.getId());
		Assert.assertNotNull(b4.getId());
		Assert.assertNotNull(b5.getId());
		Assert.assertNotNull(b6.getId());
		Assert.assertNotNull(b7.getId());
		Assert.assertNotNull(b8.getId());
		Assert.assertNotNull(b9.getId());
		
		dao = context.getBean(RetrieveDao.class);
		Assert.assertNotNull(dao);
	}
	
	@Test
	public void testLoadByPk() {
		Book book = dao.loadByPk(pk);
		Assert.assertNotNull(book);
		Assert.assertEquals(pk, book.getId());
	}
	
	@Test
	public void testLoadByTitle() {
		Book book = dao.loadByTitle("Book 0");
		Assert.assertNotNull(book);
		Assert.assertEquals(pk, book.getId());
	}
	
	@Test
	public void testLoadByQuery() {
		Book book = dao.loadByQuery("Book 0");
		Assert.assertNotNull(book);
		Assert.assertEquals(pk, book.getId());
	}
	
	@Test
	public void testLoadByNamedQuery() {
		Book book = dao.loadByNamedQuery("Book 0");
		Assert.assertNotNull(book);
		Assert.assertEquals(pk, book.getId());
	}
	
	@Test
	public void testLoadModelByQuery() {
		BookModel model = dao.loadModelByQuery(pk);
		Assert.assertNotNull(model);
		Assert.assertEquals(pk, model.getId());
	}
	
	@Test
	public void testLoadModelByNamedQuery() {
		BookModel model = dao.loadModelByNamedQuery("Book 0");
		Assert.assertNotNull(model);
		Assert.assertEquals(pk, model.getId());
	}
	
	private static Book getNewBook(String title, float price, int length) {
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
}
