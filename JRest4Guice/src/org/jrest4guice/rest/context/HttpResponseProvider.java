package org.jrest4guice.rest.context;

import javax.servlet.http.HttpServletResponse;

import com.google.inject.Provider;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ：86895156)</a>
 *
 */
public class HttpResponseProvider implements Provider<HttpServletResponse> {

	public HttpServletResponse get() {
		return HttpContextManager.getResponse();
	}
}
