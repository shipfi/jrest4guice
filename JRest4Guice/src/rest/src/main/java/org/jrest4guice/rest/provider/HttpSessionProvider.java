package org.jrest4guice.rest.provider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jrest4guice.rest.RestContextManager;

import com.google.inject.Provider;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class HttpSessionProvider implements Provider<HttpSession> {

	public HttpSession get() {
		HttpServletRequest request = RestContextManager.getRequest();
		if(request == null)
			return null;
		return request.getSession();
	}
}
