package org.jrest4guice.rest.provider;

import javax.servlet.http.HttpServletResponse;

import org.jrest4guice.rest.RestContextManager;

import com.google.inject.Provider;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class HttpResponseProvider implements Provider<HttpServletResponse> {

	public HttpServletResponse get() {
		return RestContextManager.getResponse();
	}
}
