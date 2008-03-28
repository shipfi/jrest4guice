package org.jrest.context;

import javax.servlet.http.HttpServletResponse;

import com.google.inject.Provider;

public class HttpResponseProvider implements Provider<HttpServletResponse> {

	public HttpServletResponse get() {
		return IocContextManager.getResponse();
	}
}
