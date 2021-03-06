package org.jrest.dao.test.jpa;

import java.util.List;

import org.jrest.dao.test.entities.Book;
import org.jrest4guice.dao.annotations.Create;
import org.jrest4guice.dao.annotations.Dao;
import org.jrest4guice.dao.annotations.Delete;
import org.jrest4guice.dao.annotations.Find;
import org.jrest4guice.dao.annotations.Retrieve;
import org.jrest4guice.dao.annotations.Update;

import com.google.inject.name.Named;

@Dao
public interface BookDao {

	@Create
	void create(Book... books);

	@Retrieve
	Book load(String pk);

	@Update
	void update(Book... books);

	@Delete
	void delete(Book... books);

	@Find(query = "from Book b where b.price > :price")
	List<Book> findPriceMoreThan(@Named("price")float price);

	@Find(query = "from Book b where b.packingInfo.length < ?")
	List<Book> findLengthLessThan(int length);

	@Find(query = "from Book b where b.price = :price and b.packingInfo.length = :length")
	List<Book> findPriceAndLengthEqual(@Named("price")float price, @Named("length")int length);
}
