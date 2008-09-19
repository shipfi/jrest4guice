package org.jrest4guice.rest.render;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import org.commontemplate.core.Context;
import org.commontemplate.core.Template;
import org.commontemplate.engine.Engine;
import org.jrest4guice.rest.ServiceResult;
import org.jrest4guice.rest.annotations.MimeType;
import org.jrest4guice.rest.annotations.PageFlow;
import org.jrest4guice.rest.cache.ResourceCacheManager;
import org.jrest4guice.rest.context.RestContextManager;
import org.jrest4guice.security.SecurityContext;
import org.jrest4guice.security.UserRole;

import com.google.inject.Inject;

/**
 * CTL（commontemplate）的渲染器
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 */
public class CTLViewRender implements ViewRender {
	@Inject
	private HttpServletRequest request;
	@Inject
	private Engine engine;
	@Inject
	private Context context;
	
	@Inject
	private SecurityContext securityContext;

	/* (non-Javadoc)
	 * @see org.jrest4guice.rest.render.ViewRender#render(java.io.PrintWriter, org.jrest4guice.rest.annotations.PageFlow, org.jrest4guice.rest.ServiceResult, boolean)
	 */
	@Override
	public void render(PrintWriter out, PageFlow annotation, ServiceResult result,boolean cache)
			throws Exception {
		
		try {
			String url = annotation.success().value();
			if(!result.isInChain() &&(result.getErrorType() != null || result.getInvalidValues() != null)){
				url = annotation.error().value();
			}
			
			url = this.request.getSession().getServletContext().getRealPath("/")+url;
			
			//获取模板
			Template template = this.engine.getTemplate(url);
			//往上下文中填入数据
			this.context.put("ctxPath", this.request.getContextPath());
			UserRole userPrincipal = this.securityContext.getUserPrincipal();
			this.context.put(SecurityContext.CURRENT_USER_ROlE, userPrincipal);
			this.context.put(SecurityContext.USER_IS_LOGIN, userPrincipal!=null);
			this.context.put("ctx", result);
			
			template.render(this.context);
			
			String content = this.context.getOut().toString();
			
			//输出到页面
			out.println(content);
			
			if(cache){//缓存
				ResourceCacheManager.getInstance().cacheStaticResource(RestContextManager.getCurrentRestUri(), MimeType.MIME_OF_TEXT_HTML, content.getBytes(), request);
			}
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
