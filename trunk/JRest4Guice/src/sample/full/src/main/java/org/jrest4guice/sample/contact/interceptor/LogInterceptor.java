package org.jrest4guice.sample.contact.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class LogInterceptor implements MethodInterceptor {
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		String name = invocation.getMethod().getName();
		System.out.println("[LogInterceptor]进入－》"+name);
		Object proceed = invocation.proceed();
		System.out.println("[LogInterceptor]离开－》"+name);
		return proceed;
	}
}
