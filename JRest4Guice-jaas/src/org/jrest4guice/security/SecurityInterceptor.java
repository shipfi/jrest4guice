package org.jrest4guice.security;

import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
		if (!declaringClass.isAnnotationPresent(PermitAll.class)) {
			if (method.isAnnotationPresent(RolesAllowed.class)) {
				HttpSession session = request.getSession(true);
				Subject subject = (Subject) session
						.getAttribute("javax.security.auth.subject");
				if (subject == null)
					throw new UserNotLoginException("没有登录");
				Set<Principal> principals = subject.getPrincipals();

				RolesAllowed annotation = method
						.getAnnotation(RolesAllowed.class);
				String[] roles = annotation.value();

				boolean hasPermission = false;
				top: for (Principal p : principals) {
					for (String roleName : roles) {
						if (p.getName().equalsIgnoreCase(roleName)) {
							hasPermission = true;
							break top;
						}
					}
				}

				if (!hasPermission)
					throw new AccessDeniedException("拒绝访问");

			}
		}

		// 执行被拦截的业务方法
		Object result = methodInvocation.proceed();
		// 返回业务方法的执行结果
		return result;
	}
}
