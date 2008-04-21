package org.jrest.rest.writer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public abstract class TextResponseWriter implements ResponseWriter {
	/* (non-Javadoc)
	 * @see org.jrest.rest.ResponseWriter#writeResult(javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.String)
	 */
	public void writeResult(HttpServletResponse response, Object result,String charset) {
		if (result == null)
			return;
		
		String textContent = this.generateTextContent(result);
		
		try {
			response.setCharacterEncoding(charset);
			PrintWriter out = response.getWriter();
			out.println(textContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected abstract String generateTextContent(Object result);
}
