package org.jrest4guice.sample.contact.service;

import org.jrest4guice.client.Page;
import org.jrest4guice.rest.annotations.Delete;
import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.ModelBean;
import org.jrest4guice.rest.annotations.PageFlow;
import org.jrest4guice.rest.annotations.PageInfo;
import org.jrest4guice.rest.annotations.Parameter;
import org.jrest4guice.rest.annotations.Path;
import org.jrest4guice.rest.annotations.Post;
import org.jrest4guice.rest.annotations.Put;
import org.jrest4guice.rest.annotations.RESTful;
import org.jrest4guice.sample.contact.domain.ContactServiceDomain;
import org.jrest4guice.sample.contact.entity.Contact;

import com.google.inject.Inject;

/**
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 联系人的资源对象，并声明remoteable为真（可以通过@RemoteReference的注入到任一资源对象,通常用在跨应用的资源调用上）
 */
@RESTful(name = "ContactResource", remoteable = true)
@Path( { "/contact", "/contacts/{contactId}" })
public class ContactResource {
	@Inject
	private ContactServiceDomain domain;

	/**
	 * 创建新的联系人 
	 * contact 联系人实体
	 */
	@Post
	public String createContact(@ModelBean Contact contact) {
		return this.domain.createContact(contact);
	}

	/**
	 * 修改联系人信息 
	 * contact 联系人实体
	 */
	@Put
	public void putContact(@ModelBean Contact contact) {
		this.domain.updateContact(contact);
	}

	/**
	 * 显示联系人列表 
	 * pageIndex 页码 
	 * pageSize 每页记录数
	 */
	@Get
	@Path("/contacts")
	@PageFlow(success = @PageInfo(url = "/template/contacts.vm"))
	public Page<Contact> listContacts(int pageIndex, int pageSize) {
		return this.domain.listContacts(pageIndex, pageSize);
	}

	/**
	 * 显示单个联系人的信息 
	 * contactId 联系对象ID
	 */
	@Get
	@PageFlow(success = @PageInfo(url = "/template/contactDetail.vm"))
	public Contact getContact(@Parameter("contactId") String contactId) {
		return this.domain.findContactById(contactId);
	}

	/**
	 * 删除指定ID的联系人 
	 * contactId 联系对象ID
	 */
	@Delete
	public void deleteContact(@Parameter("contactId") String contactId) {
		this.domain.deleteContact(contactId);
	}
}
