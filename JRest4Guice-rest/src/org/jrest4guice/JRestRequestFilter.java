package org.jrest4guice;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
@SuppressWarnings("unchecked")
public class JRestRequestFilter implements Filter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4393521946859930914L;

	private static Set<String> extNameExcludes;
	static {
		extNameExcludes = new HashSet<String>(11);
		extNameExcludes.add("js");
		extNameExcludes.add("css");
		extNameExcludes.add("jsp");
		extNameExcludes.add("jspa");
		extNameExcludes.add("do");
		extNameExcludes.add("html");
		extNameExcludes.add("htm");
		extNameExcludes.add("jpg");
		extNameExcludes.add("gif");
		extNameExcludes.add("png");
		extNameExcludes.add("bmp");
		extNameExcludes.add("swf");
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
	}

	@Override
	public void doFilter(ServletRequest servletReqest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletReqest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		// REST资源的参数，这些参数都包含在URL中
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

		new RequestProcessor().process(servletReqest, servletResponse);
	}

	@Override
	public void destroy() {
	}
}
