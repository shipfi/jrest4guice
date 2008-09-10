package org.jrest4guice.sna;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
public class SNASessionFileterHelper {
	
	static Log log = LogFactory.getLog(SNASessionFileterHelper.class);
	
	static final String REMOVE_ATTRIBUTE = "removeAttribute";
	static final String SET_ATTRIBUTE = "setAttribute";
	static final String GET_ATTRIBUTE = "getAttribute";
	static final String GET_SESSION = "getSession";
	static final String SESSION_NAME = "_$_sna-session-id_$_";

	/**
	 * 缓存提供者
	 */
	private CacheProvider cacheProvider;

	public SNASessionFileterHelper(CacheProvider cacheManager) {
		this.cacheProvider = cacheManager;
	}

	/**
	 * 创建HttpServletRequest的包装对象
	 * @param request
	 * @param session
	 * @return
	 */
	public HttpServletRequest createRequestWrapper(
			final HttpServletRequest request, final SNASession session) {
		final HttpSession hSession = request.getSession();
		return (HttpServletRequest) Proxy.newProxyInstance(
				HttpServletRequest.class.getClassLoader(),
				new Class[] { HttpServletRequest.class },
				new InvocationHandler() {
					private HttpSession proxySession = null;

					public Object invoke(Object proxy, Method method,
							Object[] args) throws Throwable {
						if (method.getName().equalsIgnoreCase(
								SNASessionFileterHelper.GET_SESSION)) {
							if (proxySession == null) {
								proxySession = SNASessionFileterHelper.this.createSessionWrapper(hSession,
										session);
							}
							return proxySession;
						}
						return method.invoke(request, args);
					}
				});
	}

	/**
	 * 从cache中查询会话对象
	 * @param id
	 * @return
	 */
	public SNASession getSession(String id) {
		Object value = cacheProvider.get(id);
		if (value == null) {
			return new SNASession();
		}
		return (SNASession) value;
	}

	/**
	 * 从Cookie中获取会话ID
	 * @param request
	 * @param hResponse
	 * @return
	 */
	public String getSessionId(HttpServletRequest request,
			HttpServletResponse hResponse) {
		Cookie[] cookies = request.getCookies();
		String sessionId = null;
		if (cookies != null) {
			for (int i = 0;i < cookies.length; i++) {
				if (cookies[i].getName().equalsIgnoreCase(SNASessionFileterHelper.SESSION_NAME)) {
					sessionId = cookies[i].getValue();
					break;
				}
			}
		}
		if (sessionId == null || sessionId.trim().length() == 0) {
			sessionId = request.getSession().getId();
			this.storeSNASessionId(sessionId, hResponse);
		}
		return sessionId;
	}


	/**
	 * 创建HttpSession的包装对象
	 * @param hSession
	 * @param snaSession
	 * @return
	 */
	private HttpSession createSessionWrapper(final HttpSession hSession,
			final SNASession snaSession) {
		return (HttpSession) Proxy.newProxyInstance(HttpSession.class
				.getClassLoader(), new Class[] { HttpSession.class },
				new InvocationHandler() {
					public Object invoke(Object proxy, Method method,
							Object[] args) throws Throwable {
						String methodName = method.getName();
						if (methodName
								.equalsIgnoreCase(SNASessionFileterHelper.SET_ATTRIBUTE)) {
							snaSession.put(args[0], args[1]);
							log.debug(SNASessionFileterHelper.SET_ATTRIBUTE+"＝》"+args[0]+"="+args[1]);
							return null;
						} else if (methodName
								.equalsIgnoreCase(SNASessionFileterHelper.GET_ATTRIBUTE)) {
							Object value = method.invoke(hSession, args);
							if(value == null){
								value = snaSession.get(args[0]);
							}
							log.debug(SNASessionFileterHelper.GET_ATTRIBUTE+"＝》"+args[0]+"'s value is "+value);
							return value;
						} else if (methodName
								.equalsIgnoreCase(SNASessionFileterHelper.REMOVE_ATTRIBUTE)) {
							snaSession.remove(args[0]);
							log.debug(SNASessionFileterHelper.REMOVE_ATTRIBUTE+"＝》"+args[0]);
							return null;
						}
						return method.invoke(hSession, args);
					}
				});
	}

	/**
	 * 缓存会话ID
	 * @param sessionId
	 * @param response
	 */
	private void storeSNASessionId(String sessionId, HttpServletResponse response) {
		Cookie cookie = new Cookie(SNASessionFileterHelper.SESSION_NAME, sessionId);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
