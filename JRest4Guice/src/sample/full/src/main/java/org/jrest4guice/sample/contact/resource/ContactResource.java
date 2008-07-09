package org.jrest4guice.sample.contact.resource;

import org.jrest4guice.client.Page;
import org.jrest4guice.rest.annotations.Cache;
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
import org.jrest4guice.rest.render.ViewRenderType;
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
	private ContactService domain;

	/**
	 * 创建新的联系人 
	 * @param contact 联系人实体
	 */
	@Post
	public String createContact(@ModelBean Contact contact) {
		return this.domain.createContact(contact);
	}

	/**
	 * 修改联系人信息 
	 * @param contact 联系人实体
	 */
	@Put
	public void putContact(@ModelBean Contact contact) {
		this.domain.updateContact(contact);
	}

	/**
	 * 显示联系人列表 
	 * PageFlow ：当服务端返回类型是Text/Html类型时，重定向用户的请求到指定的页面，实现最基本功能的MVC。
	 * 		在这里，指明当操作成功时，重定向到列表人列表页面，并使用Velocity模板进行渲染，当操作失败时，
	 * 		将用户请求重定向到操作出错页面。
	 * @param pageIndex 页码 
	 * @param pageSize 每页记录数
	 */
	@Get
	@Path("/contacts")
	@PageFlow(
			success = @PageInfo(url = "/template/contacts.vm",render=ViewRenderType.VELOCITY), 
			error = @PageInfo(url = "/template/error.vm",render=ViewRenderType.VELOCITY))
	public Page<Contact> listContacts(int pageIndex, int pageSize) {
		return this.domain.listContacts(pageIndex, pageSize);
	}

	/**
	 * 显示单个联系人的信息 
	 * @param contactId 联系对象ID
	 */
	@Get
	@PageFlow(success = @PageInfo(url = "/template/contactDetail.vm"))
	@Cache //声明需要缓存结果，可以减少应用服务器及数据库的压力
	public Contact getContact(@Parameter("contactId") String contactId) {
		return this.domain.findContactById(contactId);
	}

	/**
	 * 删除指定ID的联系人 
	 * @param contactId 联系对象ID
	 */
	@Delete
	public void deleteContact(@Parameter("contactId") String contactId) {
		this.domain.deleteContact(contactId);
	}
}
