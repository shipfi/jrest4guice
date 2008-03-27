package org.cnoss.jrest.test;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cnoss.jrest.HttpResult;
import org.cnoss.jrest.annotation.FirstResult;
import org.cnoss.jrest.annotation.HttpMethod;
import org.cnoss.jrest.annotation.HttpMethodType;
import org.cnoss.jrest.annotation.JndiResource;
import org.cnoss.jrest.annotation.MaxResults;
import org.cnoss.jrest.annotation.Restful;
import org.cnoss.jrest.ioc.ModelMap;
import org.cnoss.jrest.test.service.ContactService;

import com.google.inject.Inject;

@Restful(uri = "/contacts")
public class ContactListRestService {
	@Inject
	protected ModelMap modelMap;

	@Inject
	protected HttpServletRequest request;

	@Inject
	protected HttpServletResponse response;

	@Inject
	@JndiResource(jndi = "test/ContactService")
	private ContactService service;

	@HttpMethod(type = HttpMethodType.GET)
	public String getContact(@FirstResult int first, @MaxResults int max) {
		try {
			List contacts = this.service.listContacts(first, max);
			String json = HttpResult.createSuccessfulHttpResult(contacts).toJson();
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return HttpResult.createFailedHttpResult(e.getCause().getClass().getName(),e.getMessage()).toJson();
		}
	}
}
