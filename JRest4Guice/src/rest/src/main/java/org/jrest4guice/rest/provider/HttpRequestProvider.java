package org.jrest4guice.rest.provider;

import javax.servlet.http.HttpServletRequest;

import org.jrest4guice.rest.RestContextManager;

import com.google.inject.Provider;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class HttpRequestProvider implements Provider<HttpServletRequest> {

	public HttpServletRequest get() {
		return RestContextManager.getRequest();
	}
}
