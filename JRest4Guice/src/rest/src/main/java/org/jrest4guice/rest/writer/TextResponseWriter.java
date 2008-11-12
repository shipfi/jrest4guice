package org.jrest4guice.rest.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jrest4guice.rest.exception.Need2RedirectException;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
public abstract class TextResponseWriter implements ResponseWriter {

	@Inject
	protected HttpServletRequest request;
	@Inject
	protected HttpServletResponse response;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jrest4guice.ResponseWriter#writeResult(javax.servlet.http.
	 * HttpServletResponse, java.lang.Object, java.lang.String)
	 */
	public void writeResult(Method method, ByteArrayOutputStream out, Object result,String charset , Map options) throws Need2RedirectException {
		String textContent = this.generateTextContent(result);
		if(textContent == null)
			textContent = "";
		try {
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			if(out != null)
				out.write(textContent.getBytes(charset));
			else{
				response.setCharacterEncoding(charset);
				response.getWriter().println(textContent);
			}
		} catch (IOException e) {
			System.out.println("向客户端写回数据错误:\n" + e.getMessage());
			e.printStackTrace();
		}
	}

	protected abstract String generateTextContent(Object result);
}
