package org.cnoss.rest4guice.context;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Provider;

public class HttpRequestProvider implements Provider<HttpServletRequest> {

	public HttpServletRequest get() {
		return HttpContextManager.getRequest();
	}
}
