package org.jrest.dao.test.jpa;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.jrest.core.guice.GuiceContext;
import org.jrest.core.persist.jpa.JpaGuiceModuleProvider;
import org.jrest.dao.DaoModuleProvider;
import org.jrest.dao.test.entities.Author;
import org.jrest.dao.test.entities.Book;
import org.jrest.dao.test.entities.PackingInfo;
import org.junit.Test;

public class BaseTest {
	
	@Test
	public void test() {
		// DaoContext context = DaoContext.getInstance();
		// context.addScanPaths("org.jrest.dao.jpa", "org.jrest.dao.test.jpa");
		// context.init();
		// BookDao dao = context.getBean(BookDao.class);
		
		GuiceContext context = GuiceContext.getInstance();
		context.addModuleProvider(new DaoModuleProvider("org.jrest.dao.test.jpa", "org.jrest.dao.jpa"),
		        new JpaGuiceModuleProvider()).init();
		BookDao dao = context.getBean(BookDao.class);
		Assert.assertNotNull(dao);
		
		Book b1 = getNewBook("Book 1", 10f, 10);
		Book b2 = getNewBook("Book 2", 10f, 10);
		dao.create(b1, b2);
		Assert.assertNotNull(b1.getId());
		Assert.assertNotNull(b2.getId());
		
		dao.delete(b1, b2);
		b1 = dao.load(b1.getId());
		b2 = dao.load(b2.getId());
		Assert.assertNull(b1);
		Assert.assertNull(b2);
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
	
}
