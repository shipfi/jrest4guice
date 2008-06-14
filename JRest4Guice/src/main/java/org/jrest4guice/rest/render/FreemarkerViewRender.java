package org.jrest4guice.rest.render;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.jrest4guice.rest.JRestResult;

import com.google.inject.Inject;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class FreemarkerViewRender implements ViewRender {
	private static Configuration cfg;

	@Inject
	protected HttpSession session;

	private void initFreemarker() {
		if (cfg == null) {
			try {
				cfg = new Configuration();
				cfg.setDirectoryForTemplateLoading(new File(this.session
						.getServletContext().getRealPath("/")));
				cfg.setObjectWrapper(new DefaultObjectWrapper());
			} catch (IOException e) {
				throw new RuntimeException("初始化Freemarker引擎失败", e);
			}
		}
	}

	@Override
	public void render(PrintWriter out, String templateUrl, JRestResult result)
			throws Exception {

		this.initFreemarker();

		Template template = cfg.getTemplate(templateUrl, "utf-8");
		StringWriter writer = new StringWriter();

		Map context = new HashMap();
		context.put("context", result);
		template.process(context, writer);

		writer.toString();
		out.println(writer.toString());
	}

	@Override
	public String getRenderType() {
		return ViewRenderType.FREEMARKER;
	}
}
