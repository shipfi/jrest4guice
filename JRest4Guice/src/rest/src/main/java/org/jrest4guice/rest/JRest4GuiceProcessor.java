package org.jrest4guice.rest;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jrest4guice.client.ModelMap;
import org.jrest4guice.commons.lang.ClassUtils;
import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.rest.annotations.HttpMethodType;
import org.jrest4guice.rest.annotations.MimeType;
import org.jrest4guice.rest.annotations.RESTful;
import org.jrest4guice.rest.exception.RestMethodNotFoundException;
import org.jrest4guice.rest.helper.JRest4GuiceHelper;
import org.jrest4guice.rest.helper.JRestGuiceProcessorHelper;
import org.jrest4guice.rest.helper.RequestHelper;
import org.jrest4guice.rest.helper.ServiceHelper;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@SuppressWarnings("unchecked")
public class JRest4GuiceProcessor {
	private String charset;
	private String urlPrefix;

	private JRestGuiceProcessorHelper helper;

	public JRest4GuiceProcessor() {
		this.helper = new JRestGuiceProcessorHelper();
	}

	public JRest4GuiceProcessor setUrlPrefix(String urlPrefix) {
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
		this.charset = request.getCharacterEncoding();
		if (this.charset == null || charset.trim().equals("")) {
			this.charset = "UTF-8";
			try {
				request.setCharacterEncoding(charset);
			} catch (Exception e) {
			}
		}

		this.helper.setCharset(this.charset);

		String contentType = RequestHelper.getContentType(request);		

		if (!contentType.equals(MimeType.CONTENT_OF_MULTIPART_FORM_DATA) && request.getContentLength() > JRest4GuiceHelper
				.getMaxBodyPayloadSize()) {
			this.helper.writeErrorMessage(new Exception("body的大小超过最大许可范围: "
					+ JRest4GuiceHelper.getMaxBodyPayloadSize()));
			return;
		}

		String uri = request.getRequestURI();
		String original_url = uri;
		String contextPath = request.getContextPath();
		if (!contextPath.trim().equals("/") && uri.startsWith(contextPath)) {
			uri = uri.substring(contextPath.length());
		}

		if (this.urlPrefix != null)
			uri = uri.replace(this.urlPrefix, "");

		// ==================================================================
		// 处理html不支持put/delete方法的情况下通过在url中补!update与!delete
		// ==================================================================
		HttpMethodType method_type = null;
		if (uri.indexOf("!update") != -1) {
			method_type = HttpMethodType.PUT;
		}
		if (uri.indexOf("!delete") != -1) {
			method_type = HttpMethodType.DELETE;
		}

		if (method_type != null) {
			uri = uri.replace("!update", "");
			uri = uri.replace("!delete", "");
		}
		// ==================================================================

		String method = request.getMethod();

		// 针对Get类型的资源做Cache检查
		if (RESTful.METHOD_OF_GET.equalsIgnoreCase(method)) {
			if (this.helper.checkResourceCache(request, response, uri))
				return;
		}

		ModelMap<String, String> params = RestContextManager.getContext()
				.getModelMap();
		try {
			int index = uri.indexOf(RESTful.REMOTE_SERVICE_PREFIX);
			if (index != -1) {
				// 以远程服务方式调用的处理
				this.processRemoteCall(request, original_url, params);
			} else {
				// 以普通方式调用的处理
				this.processNormalCall(request, uri, original_url, method_type,
						method, params);
			}
		} catch (RestMethodNotFoundException e) {
			this.helper.writeRestServiceNotFoundMessage(request, original_url);
		}
	}

	/**
	 * 处理普通方式的调用
	 * 
	 * @param request
	 * @param uri
	 * @param original_url
	 * @param method_type
	 * @param method
	 * @param params
	 * @throws Throwable
	 */
	private void processNormalCall(HttpServletRequest request, String uri,
			String original_url, HttpMethodType method_type, String method,
			ModelMap<String, String> params) throws Throwable {
		// 从REST资源注册表中查找此URI对应的资源
		Service service = ServiceHelper.getInstance().lookupResource(uri);
		if (service != null) {
			RestContextManager.setCurrentRestUri(uri);
			
			ServiceExecutor exec = GuiceContext.getInstance().getBean(
					ServiceExecutor.class);
			// 填充参数
			this.helper.fillParameters(request, params);
			// 根据不同的请求方法调用REST对象的不同方法
			exec.execute(service, method_type == null ? this.helper
					.getHttpMethodType(method) : method_type, charset, false);
		} else {
			this.helper.writeRestServiceNotFoundMessage(request, original_url);
		}
	}

	/**
	 * 处理以远程方式进行的调用（实现对分布式资源的调用）
	 * 
	 * @param request
	 * @param original_url
	 * @param params
	 * @throws Throwable
	 */
	private void processRemoteCall(HttpServletRequest request,
			String original_url, ModelMap<String, String> params)
			throws Throwable {
		int index;
		String serviceName = request
				.getParameter(RESTful.REMOTE_SERVICE_NAME_KEY);
		String methodIndex = request
				.getParameter(RESTful.REMOTE_SERVICE_METHOD_INDEX_KEY);
		Class<?> clazz = ServiceHelper.getInstance().getRemoteService(
				serviceName);
		if (clazz != null) {
			index = Integer.parseInt(methodIndex);
			List<Method> methods = ClassUtils.getSortedMethodList(clazz);
			Service service = new Service(GuiceContext.getInstance().getBean(
					clazz), methods.get(index));

			ServiceExecutor exec = GuiceContext.getInstance().getBean(
					ServiceExecutor.class);
			// 填充参数
			this.helper.fillParameters(request, params);
			// 根据不同的请求方法调用REST对象的不同方法
			exec.execute(service, this.helper
					.getHttpMethodType(RESTful.METHOD_OF_POST), charset, true);
		} else {
			this.helper.writeRestServiceNotFoundMessage(request, original_url);
		}
	}

}
