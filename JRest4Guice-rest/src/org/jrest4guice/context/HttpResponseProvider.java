package org.jrest4guice.context;

import javax.servlet.http.HttpServletResponse;

import com.google.inject.Provider;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
public class HttpResponseProvider implements Provider<HttpServletResponse> {

	public HttpServletResponse get() {
		return HttpContextManager.getResponse();
	}
}
