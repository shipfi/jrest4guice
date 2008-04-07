package org.jrest.test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jrest.rest.annotation.HttpMethod;
import org.jrest.rest.annotation.HttpMethodType;
import org.jrest.rest.annotation.ModelBean;
import org.jrest.rest.annotation.RequestParameter;
import org.jrest.rest.annotation.Restful;
import org.jrest.rest.http.HttpResult;
import org.jrest.rest.http.ModelMap;
import org.jrest.test.entity.Contact;
import org.jrest.test.service.ContactService;

import com.google.inject.Inject;

@Restful(uri = { "/contact", "/contact/{contactId}" })
public class ContactController {
	@Inject
	private ModelMap modelMap;

	@Inject
	private HttpServletRequest request;

	@Inject
	private HttpServletResponse response;

	@Inject
	private ContactService service;

	@HttpMethod(type = HttpMethodType.POST)
	public String createContact(String name, @RequestParameter("homePhone") String homePhone, @ModelBean Contact contact) {
		if (contact == null)
			return HttpResult.createFailedHttpResult("-1","联系人信息不能为空").toJson();
		String contactId = null;
		try {
			contactId = this.service.createContact(contact);
			return HttpResult.createSuccessfulHttpResult(contactId).toJson();
		} catch (RuntimeException e) {
			return HttpResult.createFailedHttpResult(e.getClass().getName(),e.getMessage()).toJson();
		}
	}

	@HttpMethod
	public String putContact(@RequestParameter("contactId")
	String contactId, @ModelBean
	Contact contact) {
		if (contactId == null)
			return HttpResult.createFailedHttpResult("-1","没有指定对应的联系人标识符").toJson();

		try {
			this.service.updateContact(contact);
			return HttpResult.createSuccessfulHttpResult("修改成功").toJson();
		} catch (RuntimeException e) {
			return HttpResult.createFailedHttpResult(e.getClass().getName(),e.getMessage()).toJson();
		}
	}

	@HttpMethod
	public String getContact(@RequestParameter("contactId")
	String contactId) {
		try {
			Contact contactDto = this.service.findContactById(contactId);
			return HttpResult.createSuccessfulHttpResult(contactDto).toJson();
		} catch (Exception e) {
			return HttpResult.createFailedHttpResult(e.getClass().getName(),e.getMessage()).toJson();
		}
	}

	@HttpMethod
	public String deleteContact(@RequestParameter("contactId")
	String contactId) {
		try {
			this.service.deleteContact(contactId);
			return HttpResult.createSuccessfulHttpResult("删除成功").toJson();
		} catch (Exception e) {
			return HttpResult.createFailedHttpResult(e.getClass().getName(),e.getMessage()).toJson();
		}
	}
}
