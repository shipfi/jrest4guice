package org.jrest.rest.writer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;

public abstract class TextResponseWriter implements ResponseWriter {
	
	@Inject
	protected HttpServletRequest request;
	@Inject
	protected HttpServletResponse response;
	
	/* (non-Javadoc)
	 * @see org.jrest.rest.ResponseWriter#writeResult(javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.String)
	 */
	public void writeResult(Object result,String charset) {
		if (result == null)
			result = "";
		
		String textContent = this.generateTextContent(result);
		
		try {
			response.setCharacterEncoding(charset);
			response.setContentType(this.getMimeType());
			
			PrintWriter out = response.getWriter();
			out.println(textContent);
		} catch (IOException e) {
			System.out.println("向客户端写回数据错误:\n"+e.getMessage());
			e.printStackTrace();
		}
	}

	protected abstract String generateTextContent(Object result);
}
