package org.jrest4guice.sample.contact.resource;

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
import org.jrest4guice.rest.render.ResultType;
import org.jrest4guice.sample.contact.entity.Contact;
import org.jrest4guice.sample.contact.service.ContactService;

import com.google.inject.Inject;

/**
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 联系人的资源对象
 * 声明remoteable为真（可以通过@RemoteReference的注入到任一资源对象,通常用在跨应用的资源调用上）
 */
@RESTful(name = "ContactResource", remoteable = true)
@Path( { "/contact", "/contacts/{contactId}" })
public class ContactResource {
	@Inject
	private ContactService service;//注入联系人管理的服务对象

	/**
	 * 创建新的联系人 
	 * PageFlow ：当服务端返回类型是Text/Html类型时，重定向用户的请求到指定的页面，实现最基本功能的MVC。
	 * 		在这里，指明当操作成功时，重定向到/contacts，当操作失败时，将用户请求重定向到/contact。
	 * @param contact 联系人实体
	 */
	@Post
	@PageFlow(success = @PageInfo(value = "/contacts",type=ResultType.REDIRECT)
			,error=@PageInfo(value="/contact",type=ResultType.REDIRECT))
	public String createContact(@ModelBean Contact contact) {
		return this.service.createContact(contact);
	}

	/**
	 * 修改联系人信息 
	 * @param contact 联系人实体
	 */
	@Put
	@PageFlow(success = @PageInfo(value = "/contacts",type=ResultType.REDIRECT)
			,error=@PageInfo(value="/contact",type=ResultType.REDIRECT))
	public void putContact(@ModelBean Contact contact) {
		this.service.updateContact(contact);
	}

	/**
	 * 显示联系人列表 
	 * @param page 页码 
	 * @param size 每页记录数
	 */
	@Get
	@Path("/contacts")
	@PageFlow(success = @PageInfo(value = "/template/contacts.ctl"))
	public Page<Contact> listContacts(int page, int size) {
		return this.service.listContacts(page, size);
	}

	/**
	 * 显示单个联系人的信息 
	 * @param contactId 联系对象ID
	 */
	@Get
	@PageFlow(success = @PageInfo(value = "/template/contactDetail.ctl"))
	public Contact getContact(@Parameter("contactId") String contactId) {
		if(contactId == null)
			return new Contact();
		return this.service.findContactById(contactId);
	}

	/**
	 * 删除指定ID的联系人 
	 * @param contactId 联系对象ID
	 */
	@Delete
	@PageFlow(success = @PageInfo(value = "/contacts",type=ResultType.REDIRECT))
	public void deleteContact(@Parameter("contactId") String contactId) {
		this.service.deleteContact(contactId);
	}
}
