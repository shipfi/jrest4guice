package org.jrest.rest.writer;

import javax.servlet.http.HttpServletResponse;

public interface ResponseWriter {

	/**
	 * 向客户端写回服务端的输出结果
	 * @param response
	 * @param result
	 */
	public abstract void writeResult(HttpServletResponse response,
			Object result, String charset);
	
	public String getMimiType();
}