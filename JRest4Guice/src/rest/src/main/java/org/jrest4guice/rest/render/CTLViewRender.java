package org.jrest4guice.rest.render;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import org.commontemplate.core.Context;
import org.commontemplate.core.Template;
import org.commontemplate.engine.Engine;
import org.jrest4guice.rest.JRestResult;
import org.jrest4guice.rest.annotations.MimeType;
import org.jrest4guice.rest.annotations.PageFlow;
import org.jrest4guice.rest.cache.ResourceCacheManager;
import org.jrest4guice.rest.context.RestContextManager;

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

	/* (non-Javadoc)
	 * @see org.jrest4guice.rest.render.ViewRender#render(java.io.PrintWriter, org.jrest4guice.rest.annotations.PageFlow, org.jrest4guice.rest.JRestResult, boolean)
	 */
	@Override
	public void render(PrintWriter out, PageFlow annotation, JRestResult result,boolean cache)
			throws Exception {
		
		String url = annotation.success().value();
		if(!result.isInChain() &&(result.getErrorType() != null || result.getInvalidValues() != null)){
			url = annotation.error().value();
		}
		
		url = this.request.getSession().getServletContext().getRealPath("/")+url;
		
		//获取模板
		Template template = this.engine.getTemplate(url);
		//往上下文中填入数据
		context.put("ctxPath", this.request.getContextPath());
		context.put("ctx", result);
		
		template.render(context);
		
		String content = context.getOut().toString();
		
		out.println(content);
		
		if(cache){
			ResourceCacheManager.getInstance().cacheStaticResource(RestContextManager.getCurrentRestUri(), MimeType.MIME_OF_TEXT_HTML, content.getBytes(), request);
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
