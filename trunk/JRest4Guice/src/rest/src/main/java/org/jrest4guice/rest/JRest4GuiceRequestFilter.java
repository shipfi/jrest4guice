package org.jrest4guice.rest;

import java.io.IOException;
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
import javax.servlet.http.HttpSession;

import org.jrest4guice.client.ModelMap;
import org.jrest4guice.commons.http.CookieUtil;
import org.jrest4guice.rest.context.RestContextManager;
import org.jrest4guice.sna.CacheProvider;
import org.jrest4guice.sna.CacheProviderProvider;
import org.jrest4guice.sna.CacheProviderRegister;
import org.jrest4guice.sna.SNASession;
import org.jrest4guice.sna.SNASessionHelper;

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


	/**
	 * 缓存服务器的提供者
	 */
	public static final String CACHE_PROVIDER = "cacheProvider";

	/**
	 * 缓存服务器列表的参数关键字
	 */
	public static final String CACHE_SERVERS = "cacheServers";
	
	/**
	 * 缺省的缓存服务提供者
	 */
	private final String cacheProviderName = "memcached";

	/**
	 * 缓存提供者
	 */
	private CacheProvider cacheProvider;
	/**
	 * SNA会话过滤器的助手
	 */
	private SNASessionHelper helper;
	
	
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

	
		String cacheServers = config.getInitParameter(CACHE_SERVERS);
		if (cacheServers != null && !cacheServers.trim().equals("")) {
			String _cacheProvider = config.getInitParameter(CACHE_PROVIDER);
			if(_cacheProvider == null || _cacheProvider.trim().equals(""))
				_cacheProvider = cacheProviderName;
			
			//获取缓存管理的实现
			this.cacheProvider = CacheProviderRegister.getInstance().getCacheProvider(_cacheProvider);
			CacheProviderProvider.setCurrentSecurityContext(this.cacheProvider);
			
			if(this.cacheProvider == null){
				throw new ServletException("无法根据名称\""+_cacheProvider+"\"来初始化缓存提供者，请确认您有没有通过GuiceContext.useSNA()来打开SNA的支持！");
			}
			this.cacheProvider.setCacheServers(cacheServers);
			
			//初始化SNA会话助手
			this.helper = new SNASessionHelper(this.cacheProvider);
		}
	}

	@Override
	public void doFilter(ServletRequest servletReqest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest hRequest = (HttpServletRequest) servletReqest;
		HttpServletResponse hResponse = (HttpServletResponse) servletResponse;
		
		String uri = hRequest.getRequestURI();
		uri = uri.replace(hRequest.getContextPath(), "");

		// 忽略以下的文件不处理
		String _uri = uri.trim().toLowerCase();
		int index = _uri.lastIndexOf(".");
		if (index != -1) {
			String ext_name = _uri.substring(index + 1);
			if (extNameExcludes.contains(ext_name)) {
				filterChain.doFilter(hRequest, hResponse);
				return;
			}
		}

		if (uri == null || "".equals(uri) || "/".equals(uri)) {
			filterChain.doFilter(hRequest, hResponse);
			return;
		}
		
		// REST资源的参数
		ModelMap<String, String> params = new ModelMap<String, String>();

		if(this.cacheProvider!=null && this.cacheProvider.isAvailable()){
			//获取会话ID
			String sessionId = CookieUtil.getSessionId(hRequest, hResponse);
			HttpSession session = hRequest.getSession();
			//从缓存服务器获取当前的会话对象
			SNASession snaSession = this.helper.getSNASession(sessionId,session);
			session.setAttribute(CookieUtil.SESSION_NAME, sessionId);

			try {
				HttpServletRequest requestWrapper = this.helper.createRequestWrapper(hRequest, snaSession);
				// 设置上下文中的环境变量
				RestContextManager.setContext(requestWrapper, hResponse, params);
				new RequestProcessor().setUrlPrefix(this.urlPrefix).process(requestWrapper, servletResponse);
			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
				//从缓存服务器客户删除已空的会话对象
				if (snaSession.isEmpty() && snaSession.isDuty()) {
					this.cacheProvider.delete(sessionId);
				} else if (snaSession.isDuty()) {
					//将变更后的会话对象缓存回缓存服务器
					snaSession.setDuty(false);
					this.cacheProvider.put(sessionId, snaSession);
				}
				// 清除上下文中的环境变量
				RestContextManager.clearContext();
			}
		}else{
			try {
				// 设置上下文中的环境变量
				RestContextManager.setContext(hRequest, hResponse, params);
				new RequestProcessor().setUrlPrefix(this.urlPrefix).process(servletReqest, servletResponse);
			} catch (Throwable e) {
				e.printStackTrace();
			}finally{
				// 清除上下文中的环境变量
				RestContextManager.clearContext();
			}
		}
	}

	@Override
	public void destroy() {
		if (this.cacheProvider != null) {
			this.cacheProvider.shutDown();
		}
	}
}
