package org.jrest4guice.security;

import java.lang.reflect.Method;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.security.exception.AccessDeniedException;
import org.jrest4guice.security.exception.UserNotLoginException;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ：86895156)</a>
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
				Principal userPrincipal = request.getUserPrincipal();
				if (userPrincipal == null)
					throw new UserNotLoginException("没有登录");
				
				RolesAllowed annotation = method
						.getAnnotation(RolesAllowed.class);
				String[] roles = annotation.value();

				boolean hasPermission = false;
				for (String roleName : roles) {
					if (request.isUserInRole(roleName)) {
						hasPermission = true;
						break;
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
