package org.jrest4guice.rest.writer.renderer;


import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jrest4guice.rest.ServiceResult;
import org.jrest4guice.rest.annotations.PageFlow;

import com.google.inject.Inject;

/**
 * 重定向的渲染器
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 */
public class RedirectViewRender implements ViewRender {
	@Inject
	private HttpSession session;
	@Inject
	private HttpServletRequest request;
	@Inject
	protected HttpServletResponse response;

	/* (non-Javadoc)
	 * @see org.jrest4guice.rest.render.ViewRender#render(java.io.OutputStream, org.jrest4guice.rest.annotations.PageFlow, org.jrest4guice.rest.ServiceResult)
	 */
	@Override
	public void render(OutputStream out, PageFlow annotation, ServiceResult result)
			throws Exception {
		String path = annotation.success().value();
		if(result.getErrorMessage() != null || result.getInvalidValues() != null){
			path = annotation.error().value();
		}

		String contextPath = request.getContextPath();
		if(!contextPath.endsWith("/"))
			contextPath += "/";
		if(path.startsWith("/"))
			path = path.substring(1);
		
		response.sendRedirect(contextPath+ path);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jrest4guice.rest.render.ViewRender#getRenderType()
	 */
	@Override
	public String getRenderType() {
		return ResultType.REDIRECT;
	}

	@Override
	public String getRenderTypeShortName() {
		return null;
	}
}
