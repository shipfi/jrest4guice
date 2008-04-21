package org.jrest.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class ResponseWriter {
	/**
	 * 向客户端写回服务端的输出结果
	 * @param response
	 * @param result
	 */
	public void writeResult(HttpServletResponse response, Object result,String charset) {
		if (result == null)
			return;
		try {
			response.setCharacterEncoding(charset);
			PrintWriter out = response.getWriter();
			out.println(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
