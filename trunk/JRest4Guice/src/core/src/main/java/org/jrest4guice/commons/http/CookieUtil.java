package org.jrest4guice.commons.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
	public static final String SESSION_NAME = "_$_rest4g-session-id_$_";
	public static final String JAAS_INFO = "_$_rest4g-jaas-id_$_";

	/**
	 * 从Cookie中获取会话ID
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static String getSessionId(HttpServletRequest request,
			HttpServletResponse response) {
		String sessionId = getCookie(CookieUtil.SESSION_NAME,request);
		if(sessionId == null && response != null){
			sessionId = request.getSession().getId();
			CookieUtil.storeCookie(CookieUtil.SESSION_NAME, sessionId, response);
		}
		return sessionId;
	}

	/**
	 * 获取指定的cookie值
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static String getCookie(String id, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		String value = null;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equalsIgnoreCase(id)) {
					value = cookies[i].getValue();
					break;
				}
			}
		}
		return value;
	}

	/**
	 * 以缺省根路径缓存cookie值
	 * @param id
	 * @param value
	 * @param response
	 */
	public static void storeCookie(String id, String value,
			HttpServletResponse response) {
		storeCookie(id,value,"/",response);
	}
	
	/**
	 * 缓存cookie值
	 * @param id
	 * @param value
	 * @param path
	 * @param response
	 */
	public static void storeCookie(String id, String value,String path,
			HttpServletResponse response) {
		Cookie cookie = new Cookie(id, value);
		cookie.setPath(path);
		response.addCookie(cookie);
	}
}
