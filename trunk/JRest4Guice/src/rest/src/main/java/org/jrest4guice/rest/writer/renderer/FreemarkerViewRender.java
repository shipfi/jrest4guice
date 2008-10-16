package org.jrest4guice.rest.writer.renderer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.jrest4guice.rest.ServiceResult;
import org.jrest4guice.rest.annotations.PageFlow;
import org.jrest4guice.rest.writer.HtmlResponseWriter;

import com.google.inject.Inject;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * Freemarker的渲染器
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 */
public class FreemarkerViewRender implements ViewRender {
	private static Configuration cfg;

	@Inject
	protected HttpSession session;
	
	/**
	 * 初始化Freemarker的配置
	 */
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
	
	/* (non-Javadoc)
	 * @see org.jrest4guice.rest.render.ViewRender#render(java.io.OutputStream, org.jrest4guice.rest.annotations.PageFlow, org.jrest4guice.rest.ServiceResult)
	 */
	@Override
	public void render(OutputStream out, PageFlow annotation, ServiceResult result)
			throws Exception {

		this.initFreemarker();
		//获取模板
		Template template = cfg.getTemplate(annotation.success().value(), "utf-8");
		StringWriter writer = new StringWriter();
		//往上下文中填入数据
		Map context = new HashMap();
		context.put("ctx", result);
		context.put("xctx",session.getAttribute(HtmlResponseWriter.OPTION_KEY));
		template.process(context, writer);

		//输出到用户端
		writer.toString();
		out.write(writer.toString().getBytes());
	}

	/* (non-Javadoc)
	 * @see org.jrest4guice.rest.render.ViewRender#getRenderType()
	 */
	@Override
	public String getRenderType() {
		return ResultType.FREEMARKER;
	}

	@Override
	public String getRenderTypeShortName() {
		return ".ftl";
	}
}
