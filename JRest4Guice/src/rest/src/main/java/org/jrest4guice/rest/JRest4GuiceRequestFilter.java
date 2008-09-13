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

import org.jrest4guice.cache.CacheProvider;
import org.jrest4guice.cache.CacheProviderProvider;
import org.jrest4guice.cache.CacheProviderRegister;
import org.jrest4guice.client.ModelMap;
import org.jrest4guice.commons.http.CookieUtil;
import org.jrest4guice.rest.context.RestContextManager;
import org.jrest4guice.rest.sna.SNAIdRequestServlet;
import org.jrest4guice.rest.sna.SNASession;
import org.jrest4guice.rest.sna.SNASessionHelper;

public class JRest4GuiceRequestFilter implements Filter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4393521946859930914L;

	/**
	 * rest服务资源的url前缀
	 */
	private String urlPrefix;

	/**
	 * 忽略处理的资源扩展名
	 */
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
		extNameExcludes.add("htc");
	}

	/**
	 * 不需要被二次重定向的地址
	 */
	private Set<String> redirectFilter = new HashSet<String>(0);

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

	/**
	 * sna会话服务器的url
	 */
	private String snaServerUrl;

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		// 初始化需要被过滤器忽略的资源扩展名
		String _extNameExcludes = config.getInitParameter("extNameExcludes");
		if (_extNameExcludes != null && !_extNameExcludes.trim().equals("")) {
			String[] exts = _extNameExcludes.split(",");
			for (String ext : exts)
				extNameExcludes.add(ext);
		}

		// 初始化不需要被二次重定向的地址
		String _redirectFilter = config.getInitParameter("redirectFilter");
		if (_redirectFilter != null && !_redirectFilter.trim().equals("")) {
			String[] urls = _redirectFilter.split(",");
			for (String url : urls)
				redirectFilter.add(url);
		}

		// 初始化rest服务的url前缀
		this.initUrlPrefix(config);

		// 初始化缓存提供者
		this.initCacheProvider(config);
	}

	/**
	 * 过滤器的主入口
	 */
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

		//检测当前应用是否充当了sna会话服务器的职能，如果是，则直接进入下一个过滤器
		boolean snaServerUrl = this.snaServerUrl.replace(
				hRequest.getRequestURL().toString(), "").trim().equals("");

		if (uri == null || "".equals(uri) || "/".equals(uri) || snaServerUrl) {
			filterChain.doFilter(hRequest, hResponse);
			return;
		}

		if (this.cacheProvider != null && this.cacheProvider.isAvailable()) {//以SNA方式过滤当前请求
			this.doFilterWithSnaSupport(servletResponse, hRequest, hResponse,
					uri);
		} else {//以正常方式过滤当前请求
			this.doFilterWithoutSnaSupport(servletReqest, servletResponse,
					hRequest, hResponse);
		}
	}

	/**
	 * 以正常方式过滤当前请求
	 * @param servletReqest
	 * @param servletResponse
	 * @param hRequest
	 * @param hResponse
	 * @param params
	 */
	private void doFilterWithoutSnaSupport(ServletRequest servletReqest,
			ServletResponse servletResponse, HttpServletRequest hRequest,
			HttpServletResponse hResponse) {
		// REST资源的参数
		ModelMap<String, String> params = new ModelMap<String, String>();
		try {
			// 设置上下文中的环境变量
			RestContextManager.setContext(hRequest, hResponse, params);
			new RequestProcessor().setUrlPrefix(this.urlPrefix).process(
					servletReqest, servletResponse);
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			// 清除上下文中的环境变量
			RestContextManager.clearContext();
		}
	}

	/**
	 * 以SNA方式过滤当前请求
	 * @param servletResponse
	 * @param hRequest
	 * @param hResponse
	 * @param uri
	 * @param params
	 * @throws IOException
	 */
	private void doFilterWithSnaSupport(ServletResponse servletResponse,
			HttpServletRequest hRequest, HttpServletResponse hResponse,
			String uri) throws IOException {
		HttpSession session = hRequest.getSession();
		// REST资源的参数
		ModelMap<String, String> params = new ModelMap<String, String>();

		// //从cookie中获取会话ID，这种方式不能解决不同域名之前的sessionId共享
		// String snaId = CookieUtil.getSessionId(hRequest, hResponse);

		// 从当前会话中获取snaId
		String snaId = (String) session
				.getAttribute(SNAIdRequestServlet.SNA_ID);
		if (snaId == null) {// 如果没有标识，则尝试从参数中获取
			snaId = hRequest.getParameter(SNAIdRequestServlet.SNA_ID);
		}

		String queryString = hRequest.getQueryString();
		// 如果snaId为null，则重定向到sna会话服务器，获取新的snaId
		if (snaId == null || snaId.trim().equals("")) {
			if (queryString != null && !queryString.equals(""))
				queryString = "&" + queryString;
			else
				queryString = "";

			// 从SNA会话服务器上获取snaId
			hResponse.sendRedirect(this.snaServerUrl + "?sourceUrl="
					+ hRequest.getRequestURL() + ";jsessionid="
					+ session.getId() + queryString);
			return;
		}

		// 保存snaId到当前会话
		if (session.getAttribute(SNAIdRequestServlet.SNA_ID) == null) {
			session.setAttribute(SNAIdRequestServlet.SNA_ID, snaId);

			// 如果不需要被二次重定向的地址中没有包含当前url，进行二次重定向，
			// 主要是为了美化地址栏，去掉因sna会话服务器而添加的地址栏参数
			if (!this.redirectFilter.contains(uri)) {
				// 构造查询参数
				queryString = queryString.replaceAll(SNAIdRequestServlet.SNA_ID
						+ "=" + snaId, "");
				if (queryString.startsWith("&"))
					queryString = queryString.replaceFirst("&", "?");
				// 重定向回原始页面
				hResponse.sendRedirect(hRequest.getRequestURL() + queryString);
				return;
			}
		}

		// 从缓存服务器获取当前的会话对象
		SNASession snaSession = this.helper.getSNASession(snaId, session);
		session.setAttribute(CookieUtil.SESSION_NAME, snaId);

		try {
			HttpServletRequest requestWrapper = this.helper
					.createRequestWrapper(hRequest, snaSession);
			// 设置上下文中的环境变量
			RestContextManager.setContext(requestWrapper, hResponse, params);
			new RequestProcessor().setUrlPrefix(this.urlPrefix).process(
					requestWrapper, servletResponse);
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			// 从缓存服务器客户删除已空的会话对象
			if (snaSession.isEmpty() && snaSession.isDuty()) {
				this.cacheProvider.delete(snaId);
			} else if (snaSession.isDuty()) {
				// 将变更后的会话对象缓存回缓存服务器
				snaSession.setDuty(false);
				this.cacheProvider.put(snaId, snaSession);
			}
			// 清除上下文中的环境变量
			RestContextManager.clearContext();
		}
	}

	/**
	 * 初始化缓存提供者
	 * @param config
	 * @throws ServletException
	 */
	private void initCacheProvider(FilterConfig config) throws ServletException {
		String cacheServers = config.getInitParameter(CACHE_SERVERS);
		if (cacheServers != null && !cacheServers.trim().equals("")) {

			this.snaServerUrl = config.getInitParameter("snaServerUrl");
			if (this.snaServerUrl == null
					|| this.snaServerUrl.trim().equals(""))
				throw new ServletException(
						"没有为JRest4GuiceRequestFilter设置snaServerUrl属性");

			String _cacheProvider = config.getInitParameter(CACHE_PROVIDER);
			if (_cacheProvider == null || _cacheProvider.trim().equals(""))
				_cacheProvider = cacheProviderName;

			// 获取缓存管理的实现
			this.cacheProvider = CacheProviderRegister.getInstance()
					.getCacheProvider(_cacheProvider);
			CacheProviderProvider.setCurrentSecurityContext(this.cacheProvider);

			if (this.cacheProvider == null) {
				throw new ServletException(
						"无法根据名称\""
								+ _cacheProvider
								+ "\"来初始化缓存提供者，请确认您有没有通过GuiceContext.useCache()来打开Cache的支持！");
			}
			this.cacheProvider.setCacheServers(cacheServers);

			// 初始化SNA会话助手
			this.helper = new SNASessionHelper(this.cacheProvider);
		}
	}

	/**
	 * 实始化rest服务的url前缀
	 * @param config
	 */
	private void initUrlPrefix(FilterConfig config) {
		try {
			FilterInfoParser servletInfoParser = new FilterInfoParser();
			Map<String, String> filterInfos = servletInfoParser.parse(config
					.getServletContext());
			String urlPattern = filterInfos.get(config.getFilterName());
			this.urlPrefix = urlPattern;

			if (urlPattern.endsWith("*"))
				this.urlPrefix = urlPattern.substring(0,
						urlPattern.length() - 1);

			if (this.urlPrefix.equals("/"))
				this.urlPrefix = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		if (this.cacheProvider != null) {
			this.cacheProvider.shutDown();
		}
	}
}
