package org.jrest4guice.rest.writer.renderer;


import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.commontemplate.core.Context;
import org.commontemplate.core.Template;
import org.commontemplate.engine.Engine;
import org.jrest4guice.rest.ServiceResult;
import org.jrest4guice.rest.annotations.PageFlow;
import org.jrest4guice.rest.writer.HtmlResponseWriter;
import org.jrest4guice.security.SecurityContext;
import org.jrest4guice.security.UserRole;

import com.google.inject.Inject;

/**
 * CTL（commontemplate）的渲染器
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 */
class CTLViewRender implements ViewRender {
	@Inject(optional=true)
	private HttpServletRequest request;
	@Inject(optional=true)
	private Engine engine;
	@Inject(optional=true)
	private Context context;
	
	@Inject(optional=true)
	private SecurityContext securityContext;

	/* (non-Javadoc)
	 * @see org.jrest4guice.rest.render.ViewRender#render(java.io.OutputStream, org.jrest4guice.rest.annotations.PageFlow, org.jrest4guice.rest.ServiceResult)
	 */
	@Override
	public void render(OutputStream out, PageFlow annotation, ServiceResult result)
			throws Exception {
		try {
			String url = annotation.success().value();
			if(!result.isInChain() &&(result.getErrorType() != null || result.getInvalidValues() != null)){
				url = annotation.error().value();
			}
			
			HttpSession session = this.request.getSession();
			url = session.getServletContext().getRealPath("/")+url;
			
			//获取模板
			Template template = this.engine.getTemplate(url);
			//往上下文中填入数据
			this.context.put("ctxPath", this.request.getContextPath());
			UserRole userPrincipal = this.securityContext.getUserPrincipal();
			this.context.put(SecurityContext.CURRENT_USER_ROlE, userPrincipal);
			this.context.put(SecurityContext.USER_IS_LOGIN, userPrincipal!=null);
			this.context.put("ctx", result);
			this.context.put("xctx",session.getAttribute(HtmlResponseWriter.OPTION_KEY));
			
			template.render(this.context);
			
			String content = this.context.getOut().toString();
			
			//输出到页面
			out.write(content.getBytes());
		}finally{
			this.context.clear();
		}
	}

	/* (non-Javadoc)
	 * @see org.jrest4guice.rest.render.ViewRender#getRenderType()
	 */
	@Override
	public String getRenderType() {
		return ResultType.CTL;
	}

	@Override
	public String getRenderTypeShortName() {
		return ".ctl";
	}
}
