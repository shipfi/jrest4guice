package org.jrest4guice.rest.writer.renderer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.servlet.http.HttpSession;

import org.jrest4guice.rest.ServiceResult;
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
	 * @see org.jrest4guice.rest.render.ViewRender#render(java.io.OutputStream, org.jrest4guice.rest.annotations.PageFlow, org.jrest4guice.rest.ServiceResult)
	 */
	@Override
	public void render(OutputStream out, PageFlow annotation, ServiceResult result)
			throws Exception {
		FileInputStream input = null;
		BufferedReader brd = null;
		try {
			input = new FileInputStream(this.session.getServletContext().getRealPath(annotation.success().value()));
			brd = new BufferedReader(new InputStreamReader(
					input, "utf-8"));
			String line;
			while ((line = brd.readLine()) != null) {
				out.write((line+"\n").getBytes());
			}
			// 输出模块中的数据源
			out.write(("<script type=\"text/javascript\">"+"\n").getBytes());
			out.write(("  context = new Spry.Data.JSONDataSet();"+"\n").getBytes());
			out.write(("  context.setPath(\"content\")"+"\n").getBytes());
			out.write(("  context.setDataFromDoc('" + result.toJson()
					+ "');"+"\n").getBytes());
			out.write(("</script>"+"\n").getBytes());
		} finally {
			if(input != null)
				input.close();
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
