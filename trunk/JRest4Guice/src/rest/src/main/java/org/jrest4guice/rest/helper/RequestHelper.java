package org.jrest4guice.rest.helper;

import javax.servlet.http.HttpServletRequest;

import org.jrest4guice.rest.annotations.MimeType;

public class RequestHelper {

	public synchronized static String getContentType(HttpServletRequest request) {
		// 获取客户端中的请求数据类型
		String contentType = request.getContentType();
		if (contentType == null || contentType.trim().equals(""))
			contentType = MimeType.CONTENT_OF_APPLICATION_X_WWW_FORM_URLENCODED;
		return contentType;
	}

	public synchronized static String getAccepte(HttpServletRequest request) {
		// 获取客户端中的请求数据类型
		String accept = request.getHeader("accept");
		if (accept == null || accept.indexOf(MimeType.MIME_OF_ALL) != -1)
			accept = "*/*";
		accept = accept.toLowerCase();
		return accept;
	}

	public synchronized static String getMimeType(HttpServletRequest request) {
		// 获取客户端中的请求数据类型
		String accept = RequestHelper.getAccepte(request);
		// 缺省的数据返回类型
		String mimeType = accept.split(",")[0];
		if (mimeType.equals(MimeType.MIME_OF_ALL))
			mimeType = MimeType.MIME_OF_TEXT_HTML;
		return mimeType;
	}
}
