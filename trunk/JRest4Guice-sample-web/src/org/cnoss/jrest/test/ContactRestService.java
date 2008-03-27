package org.cnoss.jrest.test;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cnoss.jrest.HttpResult;
import org.cnoss.jrest.annotation.HttpMethod;
import org.cnoss.jrest.annotation.HttpMethodType;
import org.cnoss.jrest.annotation.JndiResource;
import org.cnoss.jrest.annotation.ModelBean;
import org.cnoss.jrest.annotation.RequestParameter;
import org.cnoss.jrest.annotation.Restful;
import org.cnoss.jrest.ioc.ModelMap;
import org.cnoss.jrest.test.service.ContactService;
import org.cnoss.jrest.test.service.entity.Contact;

import com.google.inject.Inject;

@Restful(uri = { "/contact", "/contact/{contactId}" })
@SuppressWarnings("unchecked")
public class ContactRestService {
	@Inject
	private ModelMap modelMap;

	@Inject
	private HttpServletRequest request;

	@Inject
	private HttpServletResponse response;

	@Inject
	@JndiResource(jndi = "test/ContactService")
	private ContactService service;

	@HttpMethod(type = HttpMethodType.POST)
	public String createContact(String name, @RequestParameter("homePhone") String homePhone, @ModelBean Contact contact) {
		if (contact == null)
			throw new RuntimeException("联系人信息不能为空");
		String contactId = null;
		try {
			contactId = this.service.createContact(contact);
			return HttpResult.createSuccessHttpResult(contactId).toJson();
		} catch (RemoteException e) {
			return HttpResult.createFaileHttpResult(e.getClass().getName(),e.getMessage()).toJson();
		}
	}

	@HttpMethod
	public String putContact(@RequestParameter("contactId")
	String contactId, @ModelBean
	Contact contact) {
		if (contactId == null)
			throw new RuntimeException("没有指定对应的联系人标识符");

		try {
			this.service.updateContact(contact);
			return HttpResult.createSuccessHttpResult("修改成功").toJson();
		} catch (RemoteException e) {
			return HttpResult.createFaileHttpResult(e.getClass().getName(),e.getMessage()).toJson();
		}
	}

	@HttpMethod
	public String getContact(@RequestParameter("contactId")
	String contactId) {
		try {
			Contact contactDto = this.service.findContactById(contactId);
			return HttpResult.createSuccessHttpResult(contactDto).toJson();
		} catch (Exception e) {
			return HttpResult.createFaileHttpResult(e.getClass().getName(),e.getMessage()).toJson();
		}
	}

	@HttpMethod
	public String deleteContact(@RequestParameter("contactId")
	String contactId) {
		try {
			this.service.deleteContact(contactId);
			return HttpResult.createSuccessHttpResult("删除成功").toJson();
		} catch (Exception e) {
			return HttpResult.createFaileHttpResult(e.getClass().getName(),e.getMessage()).toJson();
		}
	}
}
