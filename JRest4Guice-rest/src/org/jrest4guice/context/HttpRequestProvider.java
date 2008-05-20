package org.jrest4guice.context;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Provider;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
public class HttpRequestProvider implements Provider<HttpServletRequest> {

	public HttpServletRequest get() {
		return HttpContextManager.getRequest();
	}
}
