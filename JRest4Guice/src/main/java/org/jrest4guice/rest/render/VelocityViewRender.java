package org.jrest4guice.rest.render;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.jrest4guice.rest.JRestResult;

import com.google.inject.Inject;

public class VelocityViewRender implements ViewRender {
	@Inject
	protected HttpSession session;

	@Override
	public void render(PrintWriter out, String templateUrl, JRestResult result)
			throws Exception {

		Template template = Velocity.getTemplate(templateUrl, "utf-8");
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
