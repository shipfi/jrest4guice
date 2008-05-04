package org.jrest.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

@SuppressWarnings("unchecked")
public class JRestRequestServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1406117824352137757L;
	
	@Override
	public void service(ServletRequest servletReqest,
			ServletResponse servletResponse) throws IOException,
			ServletException {
		new RequestProcessor().process(servletReqest, servletResponse);
	}
}
