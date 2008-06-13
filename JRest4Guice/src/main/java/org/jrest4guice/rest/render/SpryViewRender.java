package org.jrest4guice.rest.render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.jrest4guice.rest.JRestResult;

public class SpryViewRender implements ViewRender {

	@Override
	public void render(PrintWriter out, File template, JRestResult result)
			throws Exception {
		BufferedReader brd = new BufferedReader(new InputStreamReader(
				new FileInputStream(template), "utf-8"));
		try {
			String line;
			while ((line = brd.readLine()) != null) {
				out.println(line);
			}
			// 输出模块中的数据源
			out.println("<script type=\"text/javascript\">");
			out.println("  templateView_ds = new Spry.Data.JSONDataSet();");
			out.println("  templateView_ds.setPath(\"content\")");
			out.println("  templateView_ds.setDataFromDoc('" + result.toJson()
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
