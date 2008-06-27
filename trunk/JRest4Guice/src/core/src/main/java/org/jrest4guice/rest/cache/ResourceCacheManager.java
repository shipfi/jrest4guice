package org.jrest4guice.rest.cache;

import javax.servlet.http.HttpServletRequest;

import org.jrest4guice.guice.GuiceContext;

import com.google.inject.Singleton;

@Singleton
public class ResourceCacheManager {
	/**
	 * 资源存放的上下文路径
	 */
	private static String cacheStorePath = "cache";
	
	/**
	 * 缺省的资源缓存的提供者
	 */
	private ResourceCacheProvider resourceCacheProvider = new DefaultResourceCacheProvider();
	
	public String getCacheStorePath() {
		return cacheStorePath;
	}
	
	/**
	 * 设置资源存放的上下文路径
	 * @param cacheStorePath
	 */
	public void setCacheStorePath(String cacheStorePath) {
		ResourceCacheManager.cacheStorePath = cacheStorePath;
	}
	
	/**
	 * 设置资源缓存的提供者
	 * @param resourceCacheProvider
	 */
	public void setResourceCacheProvider(ResourceCacheProvider resourceCacheProvider) {
		this.resourceCacheProvider = resourceCacheProvider;
	}

	/**
	 * 缓存静态资源
	 * @param uri		资源的标识
	 * @param mimeType	资源类型
	 * @param content	资源数据
	 * @param request
	 */
	public void cacheStaticResource(String uri, String mimeType, byte[] content,HttpServletRequest request) {
		this.resourceCacheProvider.cacheStaticResource(uri, mimeType, content, request);
	}
	
	/**
	 * 检索当前资源有没有被缓存
	 * @param url
	 * @param mimeType
	 * @param request
	 * @return
	 */
	public String findStaticCacheResource(String url, String mimeType,HttpServletRequest request) {
		return this.resourceCacheProvider.findStaticCacheResource(url, mimeType, request);
	}
	
	public void clearStaticResouceCache(String resourceId,HttpServletRequest request){
		this.resourceCacheProvider.clearStaticResouceCache(resourceId, request);
	}

	/**
	 * 返回资源缓存管理者的实例
	 * @return
	 */
	public static ResourceCacheManager getInstance(){
		return GuiceContext.getInstance().getBean(ResourceCacheManager.class);
	}
}
