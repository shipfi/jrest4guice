package org.jrest4guice.security;

import java.lang.reflect.Method;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jrest4guice.core.exception.AccessDeniedException;
import org.jrest4guice.core.exception.UserNotLoginException;
import org.jrest4guice.core.guice.GuiceContext;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 * 
 */
public class SecurityInterceptor implements MethodInterceptor {
	@Inject
	private HttpServletRequest request;

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		GuiceContext.getInstance().injectorMembers(this);
		// ======================================================
		// 进行权限拦截
		// ======================================================
		Method method = methodInvocation.getMethod();
		Class<?> declaringClass = methodInvocation.getMethod()
				.getDeclaringClass();
		System.out.println("class name : " + declaringClass.getName());

		if (!declaringClass.isAnnotationPresent(PermitAll.class)) {
			if (method.isAnnotationPresent(RolesAllowed.class)) {
				RolesAllowed annotation = method
						.getAnnotation(RolesAllowed.class);
				String[] roles = annotation.value();

				User user = (User) request.getSession().getAttribute(
						SecurityManager.USER);
				if (user == null)
					throw new UserNotLoginException();

				if (SecurityManager.getInstance().hasRole(user, roles))
					throw new AccessDeniedException();

			}
		}

		// 执行被拦截的业务方法
		Object result = null;
		try {
			result = methodInvocation.proceed();
		} catch (Exception e) {
			throw e;
		}
		// 返回业务方法的执行结果
		return result;
	}
}
