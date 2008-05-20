package org.jrest.dao.test.jpa;

import org.jrest.dao.test.entities.Book;
import org.jrest.dao.test.entities.BookModel;
import org.jrest4guice.dao.annotations.Dao;
import org.jrest4guice.dao.annotations.Retrieve;

import com.google.inject.name.Named;

@Dao
public interface RetrieveDao {
	
	@Retrieve
	Book loadByPk(String id);
	
	@Retrieve
	Book loadByTitle(@Named("title")String title);
	
	@Retrieve(query = "from Book b where b.title = ?")
	Book loadByQuery(String title);
	
	@Retrieve(namedQuery = "Book.loadByTitle")
	Book loadByNamedQuery(@Named("title")String title);

	@Retrieve(query = "select b.id, b.title, b.price from Book b where b.id = ?", resultClass = BookModel.class)
	BookModel loadModelByQuery(String pk);

	@Retrieve(namedQuery = "Book.loadByTitle", resultClass = BookModel.class)
	BookModel loadModelByNamedQuery(@Named("title")String title);
}
