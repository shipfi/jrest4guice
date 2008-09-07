package org.jrest4guice.rest.render;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.jrest4guice.rest.JRestResult;
import org.jrest4guice.rest.annotations.MimeType;
import org.jrest4guice.rest.annotations.PageFlow;
import org.jrest4guice.rest.cache.ResourceCacheManager;
import org.jrest4guice.rest.context.RestContextManager;

import com.google.inject.Inject;

/**
 * Veloction的视力渲染器
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 */
public class VelocityViewRender implements ViewRender {
	@Inject
	private HttpServletRequest request;
	@Inject
	private VelocityContext context;

	/* (non-Javadoc)
	 * @see org.jrest4guice.rest.render.ViewRender#render(java.io.PrintWriter, org.jrest4guice.rest.annotations.PageFlow, org.jrest4guice.rest.JRestResult, boolean)
	 */
	@Override
	public void render(PrintWriter out, PageFlow annotation, JRestResult result,boolean cache)
			throws Exception {
		//获取模板
		Template template = Velocity.getTemplate(annotation.success().value(), "utf-8");
		//往上下文中填入数据
		context.put("contextPath", this.request.getContextPath());
		context.put("context", result);
		//输出到用户端
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		String content = writer.toString();
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
		return ResultType.VELOCITY;
	}

	@Override
	public String getRenderTypeShortName() {
		return ".vm";
	}
}
