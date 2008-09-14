package org.jrest4guice.rest;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jrest4guice.cache.CacheProvider;
import org.jrest4guice.cache.CacheProviderProvider;
import org.jrest4guice.cache.CacheProviderRegister;
import org.jrest4guice.client.ModelMap;
import org.jrest4guice.commons.http.CookieUtil;
import org.jrest4guice.rest.context.RestContextManager;
import org.jrest4guice.rest.sna.SNAHttpServletRequest;
import org.jrest4guice.rest.sna.SNAIdRequestServlet;
import org.jrest4guice.rest.sna.SNASession;
import org.jrest4guice.rest.sna.SNASessionHelper;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
public class JRest4GuiceFilterWithSnaSupport extends AbstractJRest4GuiceFilter {
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

	@Override
	protected void executeInit(FilterConfig config) throws ServletException {
		// 初始化缓存提供者
		this.initCacheProvider(config);
	}

	@Override
	protected void executeFilter(HttpServletRequest hRequest,
			HttpServletResponse hResponse, FilterChain filterChain, String uri)
			throws IOException, ServletException {

		// 检测当前应用是否充当了sna会话服务器的职能，如果是，则直接进入下一个过滤器
		boolean snaServerUrl = this.snaServerUrl.replace(
				hRequest.getRequestURL().toString(), "").trim().equals("");
		if (snaServerUrl) {
			filterChain.doFilter(hRequest, hResponse);
			return;
		}

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

			// 构造查询参数
			queryString = queryString.replaceAll(SNAIdRequestServlet.SNA_ID
					+ "=" + snaId, "");
			if (queryString.startsWith("&"))
				queryString = queryString.replaceFirst("&", "?");
			// 重定向回原始页面
			hResponse.sendRedirect(hRequest.getRequestURL() + queryString);
			return;
		}

		// 从缓存服务器获取当前的会话对象
		SNASession snaSession = this.helper.getSNASession(snaId, session);
		session.setAttribute(CookieUtil.SESSION_NAME, snaId);

		try {
			HttpServletRequest requestWrapper = new SNAHttpServletRequest(hRequest,snaSession);
			// 设置上下文中的环境变量
			RestContextManager.setContext(requestWrapper, hResponse, params);
			new JRest4GuiceProcessor().setUrlPrefix(this.urlPrefix).process(
					requestWrapper, hResponse);
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
	 * 
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
}
