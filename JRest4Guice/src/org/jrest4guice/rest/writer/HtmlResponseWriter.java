package org.jrest4guice.rest.writer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	@Override
	public String getMimeType() {
		return MimeType.MIME_OF_TEXT_HTML;
	}

	@Override
	public void writeResult(Method method,Object result, String charset) {
		try {
			response.setCharacterEncoding(charset);
			response.setContentType(this.getMimeType());
			
			PrintWriter out = response.getWriter();

			JRestResult httpResult = JRestResult.createHttpResult(result);
			String dataStr = httpResult.toJson();
			
			ViewTemplate annotation = method.getAnnotation(ViewTemplate.class);
			String templateUrl = "";
			if(annotation == null)
				annotation = method.getDeclaringClass().getAnnotation(ViewTemplate.class);
			if(annotation == null)
				templateUrl = "/"+method.getDeclaringClass().getName();
			else{
				templateUrl = annotation.value();
			}
			
			File template = new File(request.getRealPath(templateUrl));
			if(template.exists()){
				FileReader fr = new FileReader(template);
				BufferedReader br = new BufferedReader(fr);
				try {
					String line;
					while((line=br.readLine()) != null){
						out.println(line);
					}
					out.println("<script type=\"text/javascript\">");
					out.println("  templateView_ds = new Spry.Data.JSONDataSet();");
					out.println("  templateView_ds.setPath(\"content\")");
					out.println("  templateView_ds.setDataFromDoc('"+dataStr+"');");
					out.println("</script>");
					
				}finally{
					if(br != null)
						br.close();
					if(fr != null)
						fr.close();
				}
			}else{
				out.println(dataStr);
			}
		} catch (IOException e) {
			System.out.println("向客户端写回数据错误:\n"+e.getMessage());
			e.printStackTrace();
		}
	}
}
