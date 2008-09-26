package org.jrest4guice.rest.render;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jrest4guice.rest.ServiceResult;
import org.jrest4guice.rest.annotations.PageFlow;
import org.jrest4guice.rest.writer.HtmlResponseWriter;

import com.google.inject.Inject;

/**
 * JSP的渲染器
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 */
public class JspViewRender implements ViewRender {
	@Inject
	private HttpServletRequest request;
	@Inject
	protected HttpServletResponse response;

	/* (non-Javadoc)
	 * @see org.jrest4guice.rest.render.ViewRender#render(java.io.PrintWriter, org.jrest4guice.rest.annotations.PageFlow, org.jrest4guice.rest.ServiceResult, boolean)
	 */
	@Override
	public void render(PrintWriter out, PageFlow annotation, ServiceResult result,
			boolean cache) throws Exception {
		request.setAttribute("ctx", result);
		request.setAttribute("xctx", request.getSession().getAttribute(HtmlResponseWriter.OPTION_KEY));
		request.getRequestDispatcher(annotation.success().value()).forward(this.request,
				this.response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jrest4guice.rest.render.ViewRender#getRenderType()
	 */
	@Override
	public String getRenderType() {
		return ResultType.JSP;
	}
	
	@Override
	public String getRenderTypeShortName() {
		return ".jsp";
	}
}
