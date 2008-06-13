package org.jrest4guice.rest.render;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.jrest4guice.rest.JRestResult;

public class VelocityViewRender implements ViewRender {

	public VelocityViewRender() {
		try {
			Velocity.init();
		} catch (Exception e) {
			throw new RuntimeException("初始化VelocityViewRender失败", e);
		}
	}

	@Override
	public void render(PrintWriter out, File templateFile, JRestResult result)
			throws Exception {

		Template template = Velocity.getTemplate(templateFile.getPath(),
				"utf-8");
		VelocityContext context = new VelocityContext();

		context.put("context", result);

		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		writer.toString();
		out.println(writer.toString());
	}

	@Override
	public String getRenderType() {
		return ViewRenderType.VELOCITY;
	}
}
