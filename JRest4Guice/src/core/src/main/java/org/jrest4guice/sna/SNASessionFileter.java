package org.jrest4guice.sna;

import java.io.IOException;

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
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
public class SNASessionFileter implements Filter {

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
	private SNASessionFileterHelper helper;

	@Override
	public void init(FilterConfig config) throws ServletException {
		String cacheServers = config.getInitParameter(SNASessionFileter.CACHE_SERVERS);
		if (cacheServers == null || cacheServers.trim().length() == 0) {
			throw new ServletException("没有为SNASessionFileter提供cacheServers参数.");
		}
		
		String _cacheProvider = config.getInitParameter(CACHE_PROVIDER);
		if(_cacheProvider == null || _cacheProvider.trim().equals(""))
			_cacheProvider = cacheProviderName;
		
		//获取缓存管理的实现
		this.cacheProvider = CacheProviderRegister.getInstance().getCacheProvider(_cacheProvider);
		
		if(this.cacheProvider == null){
			throw new ServletException("无法根据名称\""+_cacheProvider+"\"来初始化缓存提供者，请确认您有没有通过GuiceContext.useSNA()来打开SNA的支持！");
		}
		this.cacheProvider.setCacheServers(cacheServers);
		
		//初始化SNA会话助手
		this.helper = new SNASessionFileterHelper(this.cacheProvider);
	}

	@Override
	public void destroy() {
		if (this.cacheProvider != null) {
			this.cacheProvider.shutDown();
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hRequest = (HttpServletRequest) request;
		HttpServletResponse hResponse = (HttpServletResponse) response;

		//获取会话ID
		String sessionId = this.helper.getSessionId(hRequest, hResponse);
		//从缓存服务器获取当前的会话对象
		SNASession snaSession = this.helper.getSession(sessionId);
		try {
			//进入下个过滤器处理
			chain.doFilter(this.helper.createRequestWrapper(hRequest, snaSession),
					hResponse);
		} finally {
			//从缓存服务器客户删除已空的会话对象
			if (snaSession.isEmpty()) {
				this.cacheProvider.delete(sessionId);
			} else if (snaSession.isDuty()) {
				//将变更后的会话对象缓存回缓存服务器
				snaSession.setDuty(false);
				this.cacheProvider.put(sessionId, snaSession);
			}
		}
	}
}
