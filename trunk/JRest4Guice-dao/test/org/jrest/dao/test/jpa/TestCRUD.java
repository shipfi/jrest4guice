package org.jrest.dao.test.jpa;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jrest.dao.test.entities.Author;
import org.jrest.dao.test.entities.Book;
import org.jrest.dao.test.entities.PackingInfo;
import org.jrest4guice.dao.DynamicProxy;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {"applicationContext.xml"})
public class TestCRUD extends AbstractJUnit4SpringContextTests {

	private Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private DynamicProxy dynamicProxy;

	@Test
	public void test() {
		BookDao dao = (BookDao) dynamicProxy.createDao(BookDao.class);
		
		Book b1 = getNewBook("Book 1", 10f, 10);
		Book b2 = getNewBook("Book 2", 20f, 20);

		dao.create(b1, b2);
		log.debug("Book1 ID:" + b1.getId());
		log.debug("Book2 ID:" + b2.getId());
		
		Book o1 = dao.load(b1.getId());
		Book o2 = dao.load(b2.getId());
		Assert.assertNotNull(o1);
		Assert.assertNotNull(o2);
		
		b1.setTitle("Edited 1");
		b2.setTitle("Edited 2");
		dao.update(b1, b2);
		o1 = dao.load(b1.getId());
		o2 = dao.load(b2.getId());
		Assert.assertEquals(b1.getTitle(), o1.getTitle());
		Assert.assertEquals(b2.getTitle(), o2.getTitle());
		
		dao.delete(b1, b2);
		o1 = dao.load(b1.getId());
		o2 = dao.load(b2.getId());
		Assert.assertNull(o1);
		Assert.assertNull(o2);
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
