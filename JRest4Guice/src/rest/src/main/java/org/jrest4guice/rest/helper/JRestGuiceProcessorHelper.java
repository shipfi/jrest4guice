package org.jrest4guice.rest.helper;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jrest4guice.client.ModelMap;
import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.rest.annotations.HttpMethodType;
import org.jrest4guice.rest.annotations.MimeType;
import org.jrest4guice.rest.annotations.RESTful;
import org.jrest4guice.rest.commons.cache.ResourceCacheManager;
import org.jrest4guice.rest.exception.Need2RedirectException;
import org.jrest4guice.rest.exception.ServiceNotFoundException;
import org.jrest4guice.rest.reader.RequestContentReader;
import org.jrest4guice.rest.reader.RequestContentReaderRegister;
import org.jrest4guice.rest.writer.JsonResponseWriter;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@SuppressWarnings("unchecked")
public class JRestGuiceProcessorHelper {
	
	private String charset;
	
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * 从缓存中处理当前请求
	 * @param request
	 * @param response
	 * @param uri
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public boolean checkResourceCache(HttpServletRequest request,
			HttpServletResponse response, String uri) throws IOException,
			ServletException {
		String mimeType = RequestHelper.getMimeType(request);
		String resourceUrl = ResourceCacheManager.getInstance()
				.findStaticCacheResource(uri, mimeType, request);
		if (resourceUrl != null) {
//			HttpSession session = request.getSession();
//			String fName = resourceUrl;
//			int index = fName.lastIndexOf("/");
//			if (index != -1) {
//				fName = fName.substring(index + 1);
//			}
//			final String key = ResourceCacheProvider.ETAGS_SESSION_KEY + fName;
//			Object cached = session.getAttribute(key);
//			if (cached != null) {
//				System.out.println("not modified：" + resourceUrl);
//				response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
//				response.setHeader("Last-Modified", request
//						.getHeader("If-Modified-Since"));
//			} else {
//				System.out.println("从缓存中直接重定向：" + resourceUrl);
//				session.setAttribute(key, "true");
//				Calendar cal = Calendar.getInstance();
//				cal.set(Calendar.MILLISECOND, 0);
//				Date lastModified = cal.getTime();
//				response.setDateHeader("Last-Modified", lastModified.getTime());
				response.setContentType(mimeType);

				RequestDispatcher rd = request.getSession().getServletContext()
						.getRequestDispatcher(resourceUrl);
				rd.forward(request, response);
//			}
			return true;
		}
		return false;
	}

	public void writeRestServiceNotFoundMessage(HttpServletRequest request,
			String uri) {
		String mimeType = RequestHelper.getMimeType(request);
		String msg = "没有提供指定的Rest服务 (" + uri + ") ！";
		if (MimeType.MIME_OF_TEXT_HTML.equals(mimeType)) {
			throw new ServiceNotFoundException(msg);
		} else
			this.writeErrorMessage(new Exception(msg));
	}

	public void writeErrorMessage(Exception e) {
		try {
			GuiceContext.getInstance().getBean(JsonResponseWriter.class)
					.writeResult(null, null, e,charset, null);
		} catch (Need2RedirectException e1) {
		}
	}

	public HttpMethodType getHttpMethodType(String method) {
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
	public void fillParameters(HttpServletRequest request, ModelMap params) {
		RequestContentReader reader = RequestContentReaderRegister.getInstance().getRequestReader(RequestHelper.getContentType(request));
		if(reader != null)
			reader.readData(request, params, charset);
	}
}
