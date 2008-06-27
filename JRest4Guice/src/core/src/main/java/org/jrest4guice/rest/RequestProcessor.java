package org.jrest4guice.rest;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jrest4guice.client.ModelMap;
import org.jrest4guice.commons.lang.ClassUtils;
import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.rest.annotations.HttpMethodType;
import org.jrest4guice.rest.annotations.RESTful;
import org.jrest4guice.rest.cache.ResourceCacheManager;
import org.jrest4guice.rest.context.HttpContextManager;
import org.jrest4guice.rest.context.JRestContext;
import org.jrest4guice.rest.exception.RestMethodNotFoundException;
import org.jrest4guice.rest.writer.JsonResponseWriter;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@SuppressWarnings("unchecked")
public class RequestProcessor {
	private String charset;

	private String urlPrefix;

	public RequestProcessor setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
		return this;
	}

	/**
	 * 处理来自客户端的请求
	 * 
	 * @param servletReqest
	 * @param servletResponse
	 */
	public void process(ServletRequest servletReqest,
			ServletResponse servletResponse) throws Throwable {
		HttpServletRequest request = (HttpServletRequest) servletReqest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		// 获取字符编码
		charset = request.getCharacterEncoding();

		if (charset == null || charset.trim().equals("")) {
			charset = "UTF-8";
			try {
				request.setCharacterEncoding(charset);
			} catch (Exception e) {
			}
		}

		if (request.getContentLength() > JRest4GuiceHelper
				.getMaxBodyPayloadSize()) {
			this.writeErrorMessage(new Exception("body的大小超过最大许可范围: "
					+ JRest4GuiceHelper.getMaxBodyPayloadSize()));
			return;
		}

		String uri = request.getRequestURI();
		String uri_bak = uri;
		String contextPath = request.getContextPath();
		if (!contextPath.trim().equals("/") && uri.startsWith(contextPath)) {
			uri = uri.substring(contextPath.length());
		}

		if (this.urlPrefix != null)
			uri = uri.replace(this.urlPrefix, "");
		
		String method = request.getMethod();

		// 针对Get类型的资源做Cache检查
		if (RESTful.METHOD_OF_GET.equalsIgnoreCase(method)){
			String mimeType = RequestUtil.getMimeType(request);
			String resourceUrl = ResourceCacheManager.getInstance()
					.findStaticCacheResource(uri, mimeType, request);
			if (resourceUrl != null) {
				RequestDispatcher rd = request.getSession().getServletContext()
						.getRequestDispatcher(resourceUrl);
				rd.forward(request, response);
				return;
			}
		}

		// REST资源的参数，这些参数都包含在URL中
		ModelMap<String, String> params = new ModelMap<String, String>();
		// 设置上下文中的环境变量
		HttpContextManager.setContext(request, response, params);
		try {
			int index;
			if ((index = uri.indexOf(RESTful.REMOTE_SERVICE_PREFIX)) != -1) {// 以远程服务方式调用的处理
				String serviceName = request
						.getParameter(RESTful.REMOTE_SERVICE_NAME_KEY);
				String methodIndex = request
						.getParameter(RESTful.REMOTE_SERVICE_METHOD_INDEX_KEY);
				Class<?> clazz = JRestContext.getInstance().getRemoteService(
						serviceName);
				if (clazz != null) {
					index = Integer.parseInt(methodIndex);
					ServiceExecutor exec = GuiceContext.getInstance().getBean(
							ServiceExecutor.class);
					List<Method> methods = ClassUtils
							.getSortedMethodList(clazz);
					Service service = new Service(GuiceContext.getInstance()
							.getBean(clazz), methods.get(index));
					// 填充参数
					fillParameters(request, params, true);
					exec.execute(service, this
							.getHttpMethodType(RESTful.METHOD_OF_POST),
							charset, true);
				} else {
					this.writeRestServiceNotFoundMessage(uri_bak);
				}
			} else {// 以普通Web方式调用的处理
				// 从REST资源注册表中查找此URI对应的资源
				Service service = JRestContext.getInstance()
						.lookupResource(uri);
				if (service != null) {
					ServiceExecutor exec = GuiceContext.getInstance().getBean(
							ServiceExecutor.class);
					
					HttpContextManager.setCurrentRestUri(uri);
					
					// 填充参数
					fillParameters(request, params, false);
					// 根据不同的请求方法调用REST对象的不同方法
					exec.execute(service, this.getHttpMethodType(method),
							charset, false);
					
					
				} else {
					this.writeRestServiceNotFoundMessage(uri_bak);
				}
			}
		} catch (RestMethodNotFoundException e) {
			this.writeRestServiceNotFoundMessage(uri_bak);
		} finally {
			// 清除上下文中的环境变量
			HttpContextManager.clearContext();
		}
	}

	private void writeRestServiceNotFoundMessage(String uri) {
		this.writeErrorMessage(new Exception("没有提供指定的Rest服务 (" + uri + ") ！"));
	}

	private void writeErrorMessage(Exception e) {
		GuiceContext.getInstance().getBean(JsonResponseWriter.class)
				.writeResult(null, e, charset);
	}

	private HttpMethodType getHttpMethodType(String method) {
		if (RESTful.METHOD_OF_GET.equalsIgnoreCase(method))
			return HttpMethodType.GET;
		else if (RESTful.METHOD_OF_POST.equalsIgnoreCase(method))
			return HttpMethodType.POST;
		else if (RESTful.METHOD_OF_PUT.equalsIgnoreCase(method))
			return HttpMethodType.PUT;
		else if (RESTful.METHOD_OF_DELETE.equalsIgnoreCase(method))
			return HttpMethodType.DELETE;
		return null;
	}

	/**
	 * 填充参数
	 * 
	 * @modelMap request
	 * @modelMap params
	 */
	private void fillParameters(HttpServletRequest request, ModelMap params,
			boolean isRpc) {
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
			ServletInputStream inputStream = request.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] b = new byte[4096];
			for (int n; (n = inputStream.read(b)) != -1;) {
				baos.write(b);
			}

			if (!isRpc) {
				// URL解码
				String content = URLDecoder.decode(new String(baos
						.toByteArray()).trim(), charset);
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
			} else {
				params.put(ModelMap.RPC_ARGS_KEY, baos.toByteArray());
			}
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
