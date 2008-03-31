package org.jrest;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jrest.rest.annotation.HttpMethodType;
import org.jrest.rest.context.IocContextManager;
import org.jrest.rest.context.JRestContext;
import org.jrest.rest.context.ModelMap;
import org.jrest.rest.context.RestServiceExecutor;

@SuppressWarnings("unchecked")
public class RequestProcessor {

	public static final String METHOD_OF_GET = "get";
	public static final String METHOD_OF_POST = "post";
	public static final String METHOD_OF_PUT = "put";
	public static final String METHOD_OF_DELETE = "delete";
	
	private String charset;
	
	/**
	 * 处理来自客户端的请求
	 * @param servletReqest
	 * @param servletResponse
	 */
	public void process(ServletRequest servletReqest,ServletResponse servletResponse){
		HttpServletRequest request = (HttpServletRequest) servletReqest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		//获取字符编码
		charset = request.getCharacterEncoding();
		if (charset == null || charset.trim().equals("")){
			charset = "UTF-8";
			try {
				request.setCharacterEncoding(charset);
			} catch (UnsupportedEncodingException e) {
			}
		}

		String uri = request.getRequestURI();
		uri = uri.replace(request.getContextPath(), "");

		// REST资源的参数，这些参数都包含在URL中
		ModelMap<String,String> params = new ModelMap<String, String>();
		// 设置上下文中的环境变量
		IocContextManager.setContext(request, response, params);
		try {
			// 从REST资源注册表中查找此URI对应的资源
			Object service = JRestContext.getInstance().lookupResource(uri);
			if (service != null){
				// 填充参数
				fillParameters(request, params);
				// 根据不同的请求方法调用REST对象的不同方法
				String method = request.getMethod();
				if (METHOD_OF_GET.equalsIgnoreCase(method))
					writeResult(response, RestServiceExecutor.execute(service,
							HttpMethodType.GET));
				else if (METHOD_OF_POST.equalsIgnoreCase(method))
					writeResult(response, RestServiceExecutor.execute(service,
							HttpMethodType.POST));
				else if (METHOD_OF_PUT.equalsIgnoreCase(method))
					writeResult(response, RestServiceExecutor.execute(service,
							HttpMethodType.PUT));
				else if (METHOD_OF_DELETE.equalsIgnoreCase(method))
					writeResult(response, RestServiceExecutor.execute(service,
							HttpMethodType.DELETE));
			}
		} finally {
			// 清除上下文中的环境变量
			IocContextManager.clearContext();
		}
	}

	/**
	 * 填充参数
	 * @modelMap request
	 * @modelMap params
	 */
	private void fillParameters(HttpServletRequest request, ModelMap params) {
		Enumeration names = request.getAttributeNames();
		String name;
		while (names.hasMoreElements()) {
			name = names.nextElement().toString();
			params.put(name, request.getAttribute(name));
		}

		// url中的参数
		names = request.getParameterNames();
		while (names.hasMoreElements()) {
			name = names.nextElement().toString();
			params.put(name, request.getParameter(name));
		}

		// 以http body方式提交的参数
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					request.getInputStream(), charset));
			// Read the request
			CharArrayWriter data = new CharArrayWriter();
			char buf[] = new char[4096];
			int ret;
			while ((ret = in.read(buf, 0, 4096)) != -1)
				data.write(buf, 0, ret);

			// URL解码
			String content = URLDecoder.decode(data.toString().trim(), charset);
			// 组装参数
			if (content != "") {
				String[] param_pairs = content.split("&");
				String[] kv;
				for (String p : param_pairs) {
					kv = p.split("=");
					if (kv.length > 1)
						params.put(kv[0], kv[1]);
				}
			}

			data.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 向客户端写回服务端的输出结果
	 * @param response
	 * @param result
	 */
	private void writeResult(HttpServletResponse response, Object result) {
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
