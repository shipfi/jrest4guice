package org.jrest4guice.rest.writer;

import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jrest4guice.rest.JRestResult;
import org.jrest4guice.rest.annotations.Cache;
import org.jrest4guice.rest.annotations.MimeType;
import org.jrest4guice.rest.annotations.PageFlow;
import org.jrest4guice.rest.annotations.PageInfo;
import org.jrest4guice.rest.render.ViewRender;
import org.jrest4guice.rest.render.ViewRenderRegister;

import com.google.inject.Inject;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Annotation;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
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
			//获取模板路径
			PageFlow annotation = method.getAnnotation(PageFlow.class);
			if (annotation == null){
				writeTextPlain(out, httpResult);
			}else {
				PageInfo pageInfo = null;
				if(result instanceof Exception){
					pageInfo = annotation.error();
				}else
					pageInfo = annotation.success();
				
				//模板的渲染器
				ViewRender viewRender = ViewRenderRegister.getInstance().getViewRender(pageInfo);
				//如果模板文件存在，则调用相应的渲染器进行结果的渲染
				if(viewRender != null)
					viewRender.render(out, annotation, httpResult,method.isAnnotationPresent(Cache.class));
				else{
					writeTextPlain(out, httpResult);
				}
			}
		} catch (Exception e) {
			System.out.println("向客户端写回数据错误:\n" + e.getMessage());
			e.printStackTrace();
		}
	}

	private void writeTextPlain(PrintWriter out, JRestResult httpResult) {
		out.println(httpResult.toTextPlain());
	}
}
