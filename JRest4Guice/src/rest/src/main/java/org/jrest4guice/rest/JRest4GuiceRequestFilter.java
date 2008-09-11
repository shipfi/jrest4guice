package org.jrest4guice.rest;

import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jrest4guice.client.ModelMap;
import org.jrest4guice.rest.context.RestContextManager;

public class JRest4GuiceRequestFilter implements Filter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4393521946859930914L;

	private String urlPrefix;

	private static Set<String> extNameExcludes;
	static {
		extNameExcludes = new HashSet<String>(11);
		extNameExcludes.add("js");
		extNameExcludes.add("jsp");
		extNameExcludes.add("jspa");
		extNameExcludes.add("do");
		extNameExcludes.add("action");
		extNameExcludes.add("html");
		extNameExcludes.add("htm");
		extNameExcludes.add("jpg");
		extNameExcludes.add("gif");
		extNameExcludes.add("png");
		extNameExcludes.add("bmp");
		extNameExcludes.add("swf");
		extNameExcludes.add("css");
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// 初始化需要被过滤器忽略的资源扩展名
		String _extNameExcludes = config.getInitParameter("extNameExcludes");
		if (_extNameExcludes != null && !_extNameExcludes.trim().equals("")) {
			String[] exts = _extNameExcludes.split(",");
			for (String ext : exts)
				extNameExcludes.add(ext);
		}

		FilterInfoParser servletInfoParser = new FilterInfoParser();
		try {
			Map<String, String> filterInfos = servletInfoParser.parse(config.getServletContext());
			String urlPattern = filterInfos.get(config.getFilterName());
			this.urlPrefix = urlPattern;
			
			if(urlPattern.endsWith("*"))
				this.urlPrefix = urlPattern.substring(0,urlPattern.length()-1);
			
			if(this.urlPrefix.equals("/"))
				this.urlPrefix = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doFilter(ServletRequest servletReqest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletReqest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		Principal userPrincipal = request.getUserPrincipal();
		if(userPrincipal!=null)
			System.out.println("doFilter: "+userPrincipal.getName());

		String uri = request.getRequestURI();
		uri = uri.replace(request.getContextPath(), "");

		// 忽略以下的文件不处理
		String _uri = uri.trim().toLowerCase();
		int index = _uri.lastIndexOf(".");
		if (index != -1) {
			String ext_name = _uri.substring(index + 1);
			if (extNameExcludes.contains(ext_name)) {
				filterChain.doFilter(request, response);
				return;
			}
		}

		if (uri == null || "".equals(uri) || "/".equals(uri)) {
			filterChain.doFilter(request, response);
			return;
		}

		// REST资源的参数
		ModelMap<String, String> params = new ModelMap<String, String>();
		// 设置上下文中的环境变量
		RestContextManager.setContext(request, response, params);
		try {
			new RequestProcessor().setUrlPrefix(this.urlPrefix).process(servletReqest, servletResponse);
		} catch (Throwable e) {
			e.printStackTrace();
		}finally{
			// 清除上下文中的环境变量
			RestContextManager.clearContext();
		}
	}

	@Override
	public void destroy() {
	}
}
