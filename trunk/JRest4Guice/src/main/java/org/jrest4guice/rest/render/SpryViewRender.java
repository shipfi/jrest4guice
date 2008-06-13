package org.jrest4guice.rest.render;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpSession;

import org.jrest4guice.rest.JRestResult;

import com.google.inject.Inject;

public class SpryViewRender implements ViewRender {
	@Inject
	protected HttpSession session;

	@Override
	public void render(PrintWriter out, String templateUrl, JRestResult result)
			throws Exception {
		BufferedReader brd = new BufferedReader(new InputStreamReader(
				new FileInputStream(this.session.getServletContext().getRealPath(templateUrl)), "utf-8"));
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
		return ViewRenderType.SPRY;
	}
}
