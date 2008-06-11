package org.jrest4guice.rest;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ：86895156)</a>
 *
 */
@SuppressWarnings("unchecked")
public class JRestRequestServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1406117824352137757L;
	
	private String urlPrefix;

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletInfoParser servletInfoParser = new ServletInfoParser();
		try {
			Map<String, String> servletInfos = servletInfoParser.parse(config.getServletContext());
			String urlPattern = servletInfos.get(config.getServletName());
			this.urlPrefix = urlPattern;
			
			if(urlPattern.endsWith("*"))
				this.urlPrefix = urlPattern.substring(0,urlPattern.length()-1);
			
			if(this.urlPrefix.equals("/"))
				this.urlPrefix = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void service(ServletRequest servletReqest,
			ServletResponse servletResponse) throws IOException,
			ServletException {
		try {
			new RequestProcessor().setUrlPrefix(this.urlPrefix).process(servletReqest, servletResponse);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
