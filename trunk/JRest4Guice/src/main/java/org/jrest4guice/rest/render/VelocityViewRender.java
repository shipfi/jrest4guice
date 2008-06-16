package org.jrest4guice.rest.render;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.jrest4guice.rest.JRestResult;

import com.google.inject.Inject;

/**
 * Veloction的视力渲染器
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ：86895156)</a>
 */
public class VelocityViewRender implements ViewRender {
	@Inject
	protected HttpSession session;

	/* (non-Javadoc)
	 * @see org.jrest4guice.rest.render.ViewRender#render(java.io.PrintWriter, java.lang.String, org.jrest4guice.rest.JRestResult)
	 */
	@Override
	public void render(PrintWriter out, String templateUrl, JRestResult result)
			throws Exception {
		//获取模板
		Template template = Velocity.getTemplate(templateUrl, "utf-8");
		//往上下文中填入数据
		VelocityContext context = new VelocityContext();
		context.put("context", result);
		//输出到用户端
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		out.println(writer.toString());
	}

	/* (non-Javadoc)
	 * @see org.jrest4guice.rest.render.ViewRender#getRenderType()
	 */
	@Override
	public String getRenderType() {
		return ViewRenderType.VELOCITY;
	}
}
