package org.jrest4guice.plugin.struts2.interceptor;

import org.jrest4guice.rest.context.RestContextManager;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class JRest4GuiceLifecycleInterceptor extends AbstractInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8495328369653597266L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String result = null;
		try {
			result = invocation.invoke();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RestContextManager.clearContext();
		}
		return result;
	}
}
