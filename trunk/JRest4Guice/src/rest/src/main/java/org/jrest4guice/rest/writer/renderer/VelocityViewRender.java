package org.jrest4guice.rest.writer.renderer;

import java.io.OutputStream;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.jrest4guice.rest.ServiceResult;
import org.jrest4guice.rest.annotations.PageFlow;
import org.jrest4guice.rest.writer.HtmlResponseWriter;

import com.google.inject.Inject;

/**
 * Velocity的渲染器
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 */
public class VelocityViewRender implements ViewRender {
	@Inject
	private HttpServletRequest request;
	@Inject
	private VelocityContext context;

	/* (non-Javadoc)
	 * @see org.jrest4guice.rest.render.ViewRender#render(java.io.OutputStream, org.jrest4guice.rest.annotations.PageFlow, org.jrest4guice.rest.ServiceResult)
	 */
	@Override
	public void render(OutputStream out, PageFlow annotation, ServiceResult result,String charset)
			throws Exception {
		String url = annotation.success().value();
		if(!result.isInChain() &&(result.getErrorType() != null || result.getInvalidValues() != null)){
			url = annotation.error().value();
		}
		//获取模板
		Template template = Velocity.getTemplate(url, "utf-8");
		//往上下文中填入数据
		context.put("ctxPath", this.request.getContextPath());
		context.put("ctx", result);
		context.put("xctx",request.getSession().getAttribute(HtmlResponseWriter.OPTION_KEY));
		//输出到用户端
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		String content = writer.toString();
		out.write(content.getBytes());
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
