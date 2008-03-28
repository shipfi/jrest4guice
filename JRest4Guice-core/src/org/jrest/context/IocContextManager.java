package org.jrest.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("unchecked")
public class IocContextManager {
	// local http context (for managing the current request/scope)
	static final ThreadLocal<Context> localContext = new ThreadLocal<Context>();

	private IocContextManager() {
	}

	public static void setContext(HttpServletRequest request,
			HttpServletResponse response, ModelMap param) {
		localContext.set(new Context(request, response, param));
	}

	public static void clearContext() {
		localContext.remove();
	}

	static HttpServletRequest getRequest() {
		Context context = localContext.get();
		if (null == context) {
			throw new RuntimeException(
					"Cannot access scoped object. It appears we"
							+ " are not currently inside an HTTP Servlet request");
		}

		return context.getRequest();
	}

	static HttpServletResponse getResponse() {
		Context context = localContext.get();
		if (null == context) {
			throw new RuntimeException(
					"Cannot access scoped object. It appears we"
							+ " are not currently inside an HTTP Servlet request");
		}

		return context.getResponse();
	}

	static ModelMap getModelMap() {
		Context context = localContext.get();
		if (null == context) {
			throw new RuntimeException(
					"Cannot access scoped object. It appears we"
							+ " are not currently inside an HTTP Servlet request");
		}

		return context.getModelMap();
	}

	static class Context {
		final HttpServletRequest request;
		final HttpServletResponse response;
		final ModelMap modelMap;

		Context(HttpServletRequest request, HttpServletResponse response,
				ModelMap param) {
			this.request = request;
			this.response = response;
			this.modelMap = param;
		}

		HttpServletRequest getRequest() {
			return request;
		}

		HttpServletResponse getResponse() {
			return response;
		}

		public ModelMap getModelMap() {
			return modelMap;
		}
	}
}
