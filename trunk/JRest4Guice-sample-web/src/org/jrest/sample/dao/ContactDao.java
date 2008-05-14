package org.jrest.sample.dao;

import java.util.List;

import org.jrest.dao.annotations.Create;
import org.jrest.dao.annotations.Dao;
import org.jrest.dao.annotations.Delete;
import org.jrest.dao.annotations.Find;
import org.jrest.dao.annotations.Retrieve;
import org.jrest.dao.annotations.Update;
import org.jrest.dao.annotations.Find.FirstResult;
import org.jrest.dao.annotations.Find.MaxResults;
import org.jrest.sample.entity.Contact;

import com.google.inject.name.Named;

/**
 * 负责联人系持久化处理的DAO
 * @author cnoss
 */
@Dao
public interface ContactDao {
	@Create
	public void createContact(Contact contact);

	@Find(namedQuery="Contact.list[find]")
	public List<Contact> listContacts(@FirstResult int first,@MaxResults int max);
	
	@Retrieve
	public Contact findContactByName(@Named("name") String name);

	@Retrieve
	public Contact findContactById(@Named("id") String contactId);
	
	@Update
	public void updateContact(Contact contact);

	@Delete
	public void deleteContact(Contact contact);
}
