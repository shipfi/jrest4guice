package org.jpa4guice.test;

import java.util.List;

import javax.persistence.EntityManager;

import org.jpa4guice.annotation.Transactional;

import com.google.inject.Inject;

@SuppressWarnings("unchecked")
public class ContactService {
	@Inject
	private EntityManager entityManager;
	
	@Transactional
	public void createContact(){
		Contact contact = new Contact();
		contact.setName("cnoss");
		contact.setHomePhone("0745-39020202");
		contact.setMobilePhone("15902056701");
		contact.setEMail("zhangyouqun@gmail.com");
		this.entityManager.persist(contact);
		
		System.out.println(contact.getId());
	}
	
	public void listContact(){
		List<Contact> resultList = this.entityManager.createNamedQuery("list").getResultList();
		for(Contact contact :resultList)
			System.out.println(contact.getName()+"的邮件地址是："+contact.getEMail());
	}
}
