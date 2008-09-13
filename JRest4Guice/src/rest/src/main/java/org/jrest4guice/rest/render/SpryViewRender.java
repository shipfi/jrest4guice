package org.jrest4guice.rest.render;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpSession;

import org.jrest4guice.rest.JRestResult;
import org.jrest4guice.rest.annotations.PageFlow;

import com.google.inject.Inject;

/**
 * Spry的渲染器
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 */
public class SpryViewRender implements ViewRender {
	@Inject
	protected HttpSession session;

	/* (non-Javadoc)
	 * @see org.jrest4guice.rest.render.ViewRender#render(java.io.PrintWriter, org.jrest4guice.rest.annotations.PageFlow, org.jrest4guice.rest.JRestResult, boolean)
	 */
	@Override
	public void render(PrintWriter out, PageFlow annotation, JRestResult result,boolean cache)
			throws Exception {
		BufferedReader brd = new BufferedReader(new InputStreamReader(
				new FileInputStream(this.session.getServletContext().getRealPath(annotation.success().value())), "utf-8"));
		try {
			String line;
			while ((line = brd.readLine()) != null) {
				out.println(line);
			}
			// 输出模块中的数据源
			out.println("<script type=\"text/javascript\">");
			out.println("  context = new Spry.Data.JSONDataSet();");
			out.println("  context.setPath(\"content\")");
			out.println("  context.setDataFromDoc('" + result.toJson()
					+ "');");
			out.println("</script>");
		} finally {
			if (brd != null)
				brd.close();
		}
	}

	@Override
	public String getRenderType() {
		return ResultType.SPRY;
	}

	@Override
	public String getRenderTypeShortName() {
		return null;
	}
}