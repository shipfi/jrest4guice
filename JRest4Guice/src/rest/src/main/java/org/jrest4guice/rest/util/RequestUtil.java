package org.jrest4guice.rest.util;

import javax.servlet.http.HttpServletRequest;

import org.jrest4guice.rest.annotations.MimeType;

public class RequestUtil {

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
		String accept = RequestUtil.getAccepte(request);
		// 缺省的数据返回类型
		String mimeType = accept.split(",")[0];
		if (mimeType.equals(MimeType.MIME_OF_ALL))
			mimeType = MimeType.MIME_OF_TEXT_HTML;
		return mimeType;
	}
}
