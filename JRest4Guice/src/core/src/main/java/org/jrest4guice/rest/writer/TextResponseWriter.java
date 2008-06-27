package org.jrest4guice.rest.writer;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jrest4guice.rest.annotations.Cache;
import org.jrest4guice.rest.annotations.MimeType;
import org.jrest4guice.rest.cache.ResourceCacheManager;
import org.jrest4guice.rest.context.HttpContextManager;

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
	public void writeResult(Method method, Object result, String charset) {
		if (result == null)
			result = "";

		String textContent = this.generateTextContent(result);

		if (method.isAnnotationPresent(Cache.class))
			ResourceCacheManager.getInstance().cacheStaticResource(
					HttpContextManager.getCurrentRestUri(), this.getMimeType(),
					textContent.getBytes(), request);

		try {
			response.setCharacterEncoding(charset);
			response.setContentType(this.getMimeType());
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);

			PrintWriter out = response.getWriter();
			out.println(textContent);
		} catch (IOException e) {
			System.out.println("向客户端写回数据错误:\n" + e.getMessage());
			e.printStackTrace();
		}
	}

	protected abstract String generateTextContent(Object result);
}
