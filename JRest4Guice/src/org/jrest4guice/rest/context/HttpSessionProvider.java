package org.jrest4guice.rest.context;

import javax.servlet.http.HttpSession;

import com.google.inject.Provider;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
public class HttpSessionProvider implements Provider<HttpSession> {

	public HttpSession get() {
		return HttpContextManager.getRequest().getSession();
	}
}
