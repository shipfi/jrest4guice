package org.jrest4guice.rest.sna;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jrest4guice.commons.lang.MD5Util;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
public class SNAIdRequestServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7936159740527487586L;

	public static final String SNA_ID = "snaId";

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 获取会话ID
		String sessionId = request.getSession().getId();
		// 加密当前的会话ID
		sessionId = MD5Util.toMD5(sessionId);

		String identity = (String) request.getParameter(SNA_ID);

		// 原始URL中的参数
		String queryString = request.getQueryString();

		if (identity != null) {// 清除原来请求中的snaId标识
			queryString = queryString.replaceAll("&" + SNA_ID + "=" + identity,
					"");
		}

		// 原始URL
		String sourceUrl = request.getParameter("sourceUrl");
		// 重新拼接参数串
		queryString = queryString.replaceAll("sourceUrl=" + sourceUrl, "");
		// 重定向
		response.sendRedirect(sourceUrl + "?" + SNA_ID + "=" + sessionId
				+ queryString);
	}
}
