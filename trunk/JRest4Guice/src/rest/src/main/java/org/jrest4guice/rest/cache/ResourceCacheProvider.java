package org.jrest4guice.rest.cache;

import javax.servlet.http.HttpServletRequest;

public interface ResourceCacheProvider {
	/**
	 * 缓存静态资源
	 * @param uri		资源的标识
	 * @param mimeType	资源类型
	 * @param content	资源数据
	 * @param request
	 */
	public void cacheStaticResource(String uri, String mimeType, byte[] content,HttpServletRequest request);

	/**
	 * 检索当前资源有没有被缓存
	 * @param url
	 * @param mimeType
	 * @param request
	 * @return
	 */
	public String findStaticCacheResource(String url, String mimeType,HttpServletRequest request);

	/**
	 * 根据资源标识清除相应的缓存
	 * @param resourceId
	 */
	public void clearStaticResouceCache(String resourceId,HttpServletRequest request);
}
