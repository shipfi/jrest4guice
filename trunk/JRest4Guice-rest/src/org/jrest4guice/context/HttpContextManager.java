package org.jrest4guice.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("unchecked")
public class HttpContextManager {
	static final ThreadLocal<HttpContext> localContext = new ThreadLocal<HttpContext>();

	private HttpContextManager() {
	}

	public static void setContext(HttpServletRequest request,
			HttpServletResponse response, ModelMap param) {
		localContext.set(new HttpContext(request, response, param));
	}

	public static void clearContext() {
		localContext.remove();
	}

	static HttpServletRequest getRequest() {
		HttpContext context = localContext.get();
		if (null == context) {
			throw new RuntimeException(
					"Cannot access scoped object. It appears we"
							+ " are not currently inside an HTTP Servlet request");
		}

		return context.getRequest();
	}

	static HttpServletResponse getResponse() {
		HttpContext context = localContext.get();
		if (null == context) {
			throw new RuntimeException(
					"Cannot access scoped object. It appears we"
							+ " are not currently inside an HTTP Servlet request");
		}

		return context.getResponse();
	}

	public static ModelMap getModelMap() {
		HttpContext context = localContext.get();
		if (null == context) {
			throw new RuntimeException(
					"Cannot access scoped object. It appears we"
							+ " are not currently inside an HTTP Servlet request");
		}

		return context.getModelMap();
	}

	static class HttpContext {
		final HttpServletRequest request;
		final HttpServletResponse response;
		final ModelMap modelMap;

		HttpContext(HttpServletRequest request, HttpServletResponse response,
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
