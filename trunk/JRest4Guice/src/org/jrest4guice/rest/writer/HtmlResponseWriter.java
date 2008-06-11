package org.jrest4guice.rest.writer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jrest4guice.rest.JRestResult;
import org.jrest4guice.rest.annotations.MimeType;
import org.jrest4guice.rest.annotations.ViewTemplate;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 * 
 */
public class HtmlResponseWriter implements ResponseWriter {
	@Inject
	protected HttpServletRequest request;
	@Inject
	protected HttpServletResponse response;
	@Inject
	protected HttpSession session;

	@Override
	public String getMimeType() {
		return MimeType.MIME_OF_TEXT_HTML;
	}

	@Override
	public void writeResult(Method method, Object result, String charset) {
		try {
			response.setCharacterEncoding(charset);
			response.setContentType(this.getMimeType());

			PrintWriter out = response.getWriter();

			JRestResult httpResult = JRestResult.createHttpResult(result);
			String dataStr = httpResult.toJson();
			//获取模块路径
			ViewTemplate annotation = method.getAnnotation(ViewTemplate.class);
			String templateUrl = "";
			if (annotation == null)
				annotation = method.getDeclaringClass().getAnnotation(
						ViewTemplate.class);
			if (annotation == null)
				templateUrl = "/" + method.getDeclaringClass().getName();
			else {
				templateUrl = annotation.value();
			}
			//加载模块
			File template = new File(this.session.getServletContext().getRealPath(templateUrl));
			if (template.exists()) {
				BufferedReader brd = new BufferedReader(new InputStreamReader(
						new FileInputStream(template), "utf-8"));
				try {
					//输出模块
					String line;
					while ((line = brd.readLine()) != null) {
						out.println(line);
					}
					//输出模块中的数据源
					out.println("<script type=\"text/javascript\">");
					out.println("  templateView_ds = new Spry.Data.JSONDataSet();");
					out.println("  templateView_ds.setPath(\"content\")");
					out.println("  templateView_ds.setDataFromDoc('" + dataStr
							+ "');");
					out.println("</script>");

				} finally {
					if (brd != null)
						brd.close();
				}
			} else {
				out.println(dataStr);
			}
		} catch (IOException e) {
			System.out.println("向客户端写回数据错误:\n" + e.getMessage());
			e.printStackTrace();
		}
	}
}
