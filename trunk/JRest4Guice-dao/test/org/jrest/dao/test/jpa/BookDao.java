package org.jrest.dao.test.jpa;

import org.jrest.dao.annotations.Create;
import org.jrest.dao.annotations.Dao;
import org.jrest.dao.annotations.Delete;
import org.jrest.dao.annotations.Retrieve;
import org.jrest.dao.annotations.Update;
import org.jrest.dao.test.entities.Book;

@Dao
public interface BookDao {
	
	@Create
	void create(Book... books);
	
	@Retrieve
	Book load(String pk);
	
	@Update
	void update(Book...books);
	
	@Delete
	void delete(Book...books);

}
