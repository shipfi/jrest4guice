package org.jrest.rest;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.jrest.rest.context.ContextConfig;
import org.jrest.rest.context.JRestContext;

@SuppressWarnings("unchecked")
public class JRestRequestServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1406117824352137757L;
	
	private RequestProcessor requestProcessor;

	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			this.requestProcessor = new RequestProcessor();
		} catch (Exception e) {
			throw new ServletException("初始化JRestRequestServlet失败！",e);
		}
	}

	@Override
	public void service(ServletRequest servletReqest,
			ServletResponse servletResponse) throws IOException,
			ServletException {
		this.requestProcessor.process(servletReqest, servletResponse);
	}

	@Override
	public void destroy() {
		this.requestProcessor = null;
	}
}
