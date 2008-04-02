package org.jrest.dao.test.jpa;

import java.util.List;

import org.jrest.core.transaction.annotations.Transactional;
import org.jrest.dao.annotations.Create;
import org.jrest.dao.annotations.Dao;
import org.jrest.dao.annotations.Delete;
import org.jrest.dao.annotations.Find;
import org.jrest.dao.annotations.Retrieve;
import org.jrest.dao.annotations.Update;
import org.jrest.dao.test.entities.Book;

import com.google.inject.name.Named;

@Dao
public interface BookDao {
	
	@Transactional
	@Create
	void create(Book... books);
	
	@Retrieve
	Book load(String pk);
	
	@Update
	@Transactional
	void update(Book...books);
	
	@Delete
	@Transactional
	void delete(Book...books);
	
	@Find(query = "from Book b where b.price > :price")
	List<Book> findPriceMoreThan(@Named("price")float price);
	
	@Find(query = "from Book b where b.packingInfo.length < ?")
	List<Book> findLengthLessThan(int length);

	@Find(query = "from Book b where b.price = :price and b.packingInfo.length = :length")
	List<Book> findPriceAndLengthEqual(@Named("price")float price, @Named("length")int length);
}
