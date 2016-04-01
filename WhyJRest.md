#### jrest4guice is a lightweight、Restful service framework, and it supports JPA、JAAS and distributed resource object. ####

---

**Features:**
  * Based on Google guice
  * Zero configuration，scan and register services automatically
  * Noninvasive, users needn’t implement certain interface to achieve Restful service(just use @RESTful annotation)
  * Support  Post、Get、Put、Delete operation
  * Support caching mechanisms for Get operation，staticize dynamic resource（use @Cache annotation to declare）
  * Flexible injection(support context request/response/session and automatic injection of parameters)
  * Return different types of data(like xml/json/html) according to the different client
  * Use @PageFlow to achieve the support of MVC module2, the output can be CTE、Velocity、Freemarker and Spry template engine(only when return type is text/html, can it be valid )
  * Support JPA, use enhancive BaseEntityManager to achieve the CRUD of entity
  * Support  transaction, use @Transactional annotation to declare the type of transaction
  * Support JAAS,use @RolesAllowed annotation to manipulate the needed rose
  * Support Hibernate validator
  * Support interceptor
  * Support distributed resource object, achieve distributed deployment of business logic
  * Support plugin to integrate with Struts2

**Code example:**
```

//=======================================================
//resource class
//=======================================================

/**
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * resource object of the contact
 * set remoteable to true（use @RemoteReference annotation to inject into any 
 * resource object, and it usually used to resource invoke between cross-applications）
 */
@RESTful(name = "ContactResource", remoteable = true)
@Path( { "/contact", "/contacts/{contactId}" })
public class ContactResource {
	@Inject
	private ContactService service;//inject contact management service

	/**
	 * create a new contact
	 * PageFlow ： When server-end returns Text/Html, it redirects user’s request to given page, achieve the most basic of MVC function.
	 * Here, if success, redirect the user request to "/contacts";if failure, redirect it to "/contact"
	 * @param contact
	 */
	@Post
	@PageFlow(success = @PageInfo(value = "/contacts",type=ResultType.REDIRECT)
			,error=@PageInfo(value="/contact",type=ResultType.REDIRECT))
	public String createContact(@ModelBean Contact contact) {
		return this.service.createContact(contact);
	}

	/**
	 * modify contact information 
	 * @param contact
	 */
	@Put
	@PageFlow(success = @PageInfo(value = "/contacts",type=ResultType.REDIRECT)
			,error=@PageInfo(value="/contact",type=ResultType.REDIRECT))
	public void putContact(@ModelBean Contact contact) {
		this.service.updateContact(contact);
	}

	/**
	 * show contacts list 
	 * @param page  page number 
	 * @param size  number of records per page
	 */
	@Get
	@Path("/contacts")
	@PageFlow(success = @PageInfo(value = "/template/contacts.ctl"))
	public Page<Contact> listContacts(int page, int size) {
		return this.service.listContacts(page, size);
	}

	/**
	 * show specific contact information 
	 * @param contactId 
	 */
	@Get
	@PageFlow(success = @PageInfo(value = "/template/contactDetail.ctl"))
	public Contact getContact(@Parameter("contactId") String contactId) {
		if(contactId == null)
			return new Contact();
		return this.service.findContactById(contactId);
	}

	/**
	 * delete specific contact information 
	 * @param contactId
	 */
	@Delete
	@PageFlow(success = @PageInfo(value = "/contacts",type=ResultType.REDIRECT))
	public void deleteContact(@Parameter("contactId") String contactId) {
		this.service.deleteContact(contactId);
	}
}


//=======================================================
//business class
//=======================================================

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
@Transactional//Default value is TransactionalType.REQUIRED，it can be overrided  in method
@Interceptors({//self-defining interceptors(class-level，it works on all methods，and can be override in method）
	@Interceptor(TestInterceptor.class),
	@Interceptor(LogInterceptor.class)
})
public class ContactService{
	//inject entry management
	@Inject
	private BaseEntityManager<String, Contact> entityManager;

	public String createContact(Contact contact) {
		if (contact == null)
			throw new RuntimeException("the content of contact can not be null");

		if (this.entityManager.loadByNamedQuery("byName", contact.getName()) != null) {
			throw new RuntimeException("You have input a duplicate contact name，please enter again");
		}

		this.entityManager.create(contact);
		return contact.getId();
	}

	public void deleteContact(String contactId) {
		String[] ids = contactId.split(",");
		Contact contact;
		for (String id : ids) {
			contact = this.findContactById(id);
			if (contact == null)
				throw new RuntimeException("contact not exist!");
			this.entityManager.delete(contact);
		}
	}

	@Transactional(type=TransactionalType.READOLNY)
	public Contact findContactById(String contactId) {
		return this.entityManager.load(contactId);
	}

	@Transactional(type=TransactionalType.READOLNY)//override class-level TransactionalType to READOLNY
	@Interceptor(ListContactsInterceptor.class)//override class-level Interceptor
	public Page<Contact> listContacts(int pageIndex, int pageSize)
			throws RuntimeException {
		return this.entityManager.pageByNamedQuery("list",
				new Pagination(pageIndex, pageSize));
	}

	public void updateContact(Contact contact) {
		if (contact == null)
			throw new RuntimeException("the content of contact can not be null");
		
		Contact tmpContact = this.entityManager.loadByNamedQuery("byName", contact.getName());
		if(tmpContact != null && !contact.getId().equals(tmpContact.getId()))
			throw new RuntimeException("You have input a duplicate contact name，please enter again");

		this.entityManager.update(contact);
	}
}


//=======================================================
//a case of remote call
//=======================================================

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
@Path( { "/testCallRemote"})
public class TestRemoteResource {
	@Inject
	@RemoteReference//inject remote resource object
	private ContactResource service;

	@Get
	public Page<Contact> listContacts(int page, int size) {
		return this.service.listContacts(page, size);
	}
}


```


---

**you can get the JRest4Guice source code from svn(use maven)**

**We sincerely hope that you will give us your valuable criticisms and suggestions,contact information:**
  * Email：zhangyouqun@gmail.com
  * QQ: 86895156
  * MSN: zhangyouqun@hotmail.com