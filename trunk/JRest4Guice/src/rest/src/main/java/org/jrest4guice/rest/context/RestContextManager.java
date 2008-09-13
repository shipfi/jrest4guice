package org.jrest4guice.rest.context;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.commontemplate.core.Context;
import org.commontemplate.engine.Engine;
import org.jrest4guice.client.ModelMap;
import org.jrest4guice.guice.GuiceContext;


/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
@SuppressWarnings("unchecked")
// TODO 将不同的上下文提供一个统一的接口获取与释放
public class RestContextManager {
	static final ThreadLocal<HttpContext> httpContext = new ThreadLocal<HttpContext>();
	static final ThreadLocal<String> currentRestUri = new ThreadLocal<String>();
	static final ThreadLocal<VelocityContext> velocityContext = new ThreadLocal<VelocityContext>();
	static final ThreadLocal<Map> freemarkderContext = new ThreadLocal<Map>();
	static final ThreadLocal<Context> ctlContext = new ThreadLocal<Context>();
	
	//CTL引擎
	public static Engine ctlEngine;
	
	/**
	 * 设置CTL引擎
	 * @param engine
	 */
	public static void setCTLEngine(Engine engine){
		if(ctlEngine == null)
			ctlEngine = engine;
	}
	
	public static Context getCTLContext(){
		Context context = ctlContext.get();
		if(context == null){
			context = ctlEngine.createContext(new StringWriter());
			ctlContext.set(context);
		}
		return context;
	}

	public static VelocityContext getVelocityContext(){
		VelocityContext context = velocityContext.get();
		if(context == null){
			context = new VelocityContext();
			velocityContext.set(context);
		}
		return context;
	}

	public static Map getFreemarkerContext(){
		Map context = freemarkderContext.get();
		if(context == null){
			context = new HashMap();
			freemarkderContext.set(context);
		}
		return context;
	}

	public static void setCurrentRestUri(String url){
		currentRestUri.set(url);
	}
	
	public static String getCurrentRestUri(){
		return currentRestUri.get();
	}
	
	private RestContextManager() {
	}

	public static HttpContext getContext(){
		return httpContext.get();
	}

	public static void setContext(HttpServletRequest request,
			HttpServletResponse response, ModelMap param) {
		httpContext.set(new HttpContext(request, response, param));
	}

	public static void clearContext() {
		httpContext.remove();
		currentRestUri.remove();
		velocityContext.remove();
		ctlContext.remove();
		freemarkderContext.remove();
		GuiceContext.getInstance().closePersistenceContext();
	}

	public static HttpServletRequest getRequest() {
		HttpContext context = httpContext.get();
		if (null == context) {
			return null;
		}

		return context.getRequest();
	}

	public static HttpServletResponse getResponse() {
		HttpContext context = httpContext.get();
		if (null == context) {
			return null;
		}

		return context.getResponse();
	}

	public static ModelMap getModelMap() {
		HttpContext context = httpContext.get();
		if (null == context) {
			return null;
		}

		return context.getModelMap();
	}

	public static class HttpContext {
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
