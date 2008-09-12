package org.jrest4guice.security;

import java.lang.reflect.Method;

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
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
public class SecurityInterceptor implements MethodInterceptor {
	@Inject
	private SecurityContext securityContext;
	@Inject
	protected HttpServletRequest request;
	
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		GuiceContext.getInstance().injectorMembers(this);
		GuiceContext.getInstance().injectorMembers(this.securityContext);

		// ======================================================
		// 进行权限拦截
		// ======================================================
		Method method = methodInvocation.getMethod();
		Class<?> declaringClass = methodInvocation.getMethod()
				.getDeclaringClass();
		if (!declaringClass.isAnnotationPresent(PermitAll.class)) {
			if (method.isAnnotationPresent(RolesAllowed.class)) {
				UserRole userPrincipal = securityContext.getUserPrincipal();
				if (userPrincipal == null && request.getUserPrincipal() == null)
					throw new UserNotLoginException("没有登录");
				
				RolesAllowed annotation = method
						.getAnnotation(RolesAllowed.class);
				String[] roles = annotation.value();

				boolean hasPermission = securityContext.isUserInRole(roles);

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
