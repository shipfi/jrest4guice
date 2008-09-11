package org.jrest4guice.security;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jrest4guice.sna.CacheProvider;

import com.google.inject.Inject;

public class SecurityContext {
	@Inject
	private CacheProvider cacheProvider;
	@Inject
	protected HttpServletRequest request;
	@Inject
	protected HttpSession session;

	private UserRole userRole;

	/**
	 * 获取当前用户的权限信息
	 * 
	 * @return
	 */
	public final UserRole getUserPrincipal() {
		if (this.userRole != null)
			return this.userRole;

		if (this.cacheProvider == null || this.request == null)
			return null;
		Object uRoleObj = this.session
				.getAttribute(CacheProvider.USER_PRINCIPAL_CACHE_KEY_PREFIX);
		if (uRoleObj == null) {
			Principal userPrincipal = this.request.getUserPrincipal();
			if (userPrincipal != null) {
				String userName = userPrincipal.getName();
				String cacheName = CacheProvider.USER_PRINCIPAL_CACHE_KEY_PREFIX
						+ userName;
				uRoleObj = this.cacheProvider.get(cacheName);

				this.session
						.setAttribute(
								CacheProvider.USER_PRINCIPAL_CACHE_KEY_PREFIX,
								uRoleObj);

				this.clearUserPrincipalCache(userName);
			}
		}
		if (uRoleObj != null && uRoleObj instanceof UserRole)
			return (UserRole) uRoleObj;
		return null;
	}

	/**
	 * 判断当前请求有没有指定的角色许可
	 * 
	 * @param roles
	 * @return
	 */
	public boolean isUserInRole(String... roles) {
		UserRole userPrincipal = this.getUserPrincipal();

		if (roles == null || userPrincipal == null)
			return false;

		// 如果从缓存服务器中不能获取当前用户的权限信息，就从依赖原始的request实现
		boolean result = false;
		List<Role> uRoles = userPrincipal.getRoles();
		top: for (String role : roles) {
			for (Role r : uRoles) {
				if (role.equalsIgnoreCase(r.getName())) {
					result = true;
					break top;
				}
			}
		}

		return result;
	}
	
	/**
	 * 保存用户的权限信息
	 * @param userName
	 * @param userRole
	 */
	public void storeUserPrincipal(String userName,UserRole userRole){
		this.cacheProvider.put(CacheProvider.USER_PRINCIPAL_CACHE_KEY_PREFIX+userName, userRole);
	}

	/**
	 * 清除用户的权限信息
	 * @param userName
	 */
	public void clearUserPrincipalCache(String userName){
		this.cacheProvider.delete(CacheProvider.USER_PRINCIPAL_CACHE_KEY_PREFIX+userName);
	}
}
