package org.jrest4guice.sna;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
public interface CacheProvider {
	
	public static String USER_PRINCIPAL_CACHE_KEY_PREFIX = "_$_userPrincipal_$_";
	
	
	/**
	 * 返回当前提供者的名称
	 * @return
	 */
	public String getName();
	
	/**
	 * 设置缓存服务器的地址列表
	 * @param servers
	 */
	public void setCacheServers(String servers);
	
	/**
	 * 返回指定key的cache对象
	 * @param key
	 * @return
	 */
	public Object get(String key);

	/**
	 * 将指定的对象value以key为主键缓存到cache服务器
	 * @param key
	 * @param value
	 * @return
	 */
	public CacheProvider put(String key, Object value);

	/**
	 * 删除指定的缓存对象
	 * @param key
	 */
	public void delete(String key);

	/**
	 * 关闭缓存
	 */
	public void shutDown();

}
