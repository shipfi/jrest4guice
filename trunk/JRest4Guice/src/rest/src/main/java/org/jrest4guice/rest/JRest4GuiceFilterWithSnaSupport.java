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
import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.rest.commons.sna.SNAIdRequestServlet;
import org.jrest4guice.rest.commons.sna.SNASession;
import org.jrest4guice.rest.commons.sna.SNASessionHelper;
import org.jrest4guice.rest.commons.sna.SNASessionProvider;
import org.jrest4guice.rest.context.RestContextManager;
import org.jrest4guice.rest.exception.ServiceNotFoundException;
import org.jrest4guice.security.SecurityContext;

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

	@Override
	protected void executeInit(FilterConfig config) throws ServletException {
		// 初始化缓存提供者
		this.initCacheProvider(config);
	}

	@Override
	protected void executeFilter(HttpServletRequest hRequest,
			HttpServletResponse hResponse, FilterChain filterChain, String uri)
			throws IOException, ServletException {
		
		Throwable error = null;

		HttpSession session = hRequest.getSession();
		// REST资源的参数
		ModelMap<String, String> params = new ModelMap<String, String>();

		// 从当前会话中获取snaId
		String snaId = (String) session
				.getAttribute(SNAIdRequestServlet.SNA_ID);

		// 从缓存服务器获取当前的会话对象
		SNASession snaSession = this.helper.getSNASession(snaId, session);
		session.setAttribute(CookieUtil.SESSION_NAME, snaId);

		try {
			HttpServletRequest requestWrapper = this.helper
					.createRequestWrapper(hRequest);
			// 设置上下文中的环境变量
			RestContextManager.setContext(requestWrapper, hResponse, params);
			SNASessionProvider.setCurrentSNASession(snaSession);

			// 检测用户当前的安全状态
			if (requestWrapper.getUserPrincipal() != null) {
				SecurityContext securityContext = GuiceContext.getInstance()
						.getBean(SecurityContext.class);
				GuiceContext.getInstance().injectorMembers(securityContext);
				securityContext.getUserPrincipal();
			}

			// 处理当前请求
			new JRest4GuiceProcessor().setUrlPrefix(this.urlPrefix).process(
					requestWrapper, hResponse);
		} catch (Throwable e) {
			error = e;
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
			SNASessionProvider.clearCurrentSNASession();
		}
		
		if(error != null){
			if(error instanceof ServiceNotFoundException){
				filterChain.doFilter(hRequest, hResponse);
			}else
				throw new ServletException("JRest4GuiceFilterWithSnaSupport 拦截异常",error);
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

			String _cacheProvider = config.getInitParameter(CACHE_PROVIDER);
			if (_cacheProvider == null || _cacheProvider.trim().equals(""))
				_cacheProvider = cacheProviderName;

			// 获取缓存管理的实现
			this.cacheProvider = CacheProviderRegister.getInstance()
					.getCacheProvider(_cacheProvider);
			CacheProviderProvider.setCurrentCacheProvider(this.cacheProvider);

			if (this.cacheProvider == null) {
				throw new ServletException(
						"无法根据名称\""
								+ _cacheProvider
								+ "\"来初始化缓存提供者，请确认您有没有通过GuiceContext.useCache()来打开Cache的支持！");
			}
			this.cacheProvider.setCacheServers(cacheServers);
			// this.cacheProvider.setExpiryTime(this.sessionTimeOut*1000);

			// 初始化SNA会话助手
			this.helper = new SNASessionHelper(this.cacheProvider);
		}
	}
}
