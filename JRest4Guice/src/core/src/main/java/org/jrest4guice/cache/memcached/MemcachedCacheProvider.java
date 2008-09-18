package org.jrest4guice.cache.memcached;

import java.util.Date;

import org.jrest4guice.cache.CacheProvider;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

public class MemcachedCacheProvider implements CacheProvider {
	
	private MemCachedClient memCachedClient = null;
	private SockIOPool sockIOPool = null;
	
	/**
	 * 缺省超时时间为2小时
	 */
	private long expiryTime = 1000*60*60*2;
	
	
	/* (non-Javadoc)
	 * @see org.jrest4guice.sna.CacheProvider#setCacheServers(java.lang.String)
	 */
	@Override
	public void setCacheServers(String servers) {
		this.sockIOPool = SockIOPool.getInstance();
		this.sockIOPool.setServers(servers.split(","));

		this.sockIOPool.setInitConn(5);
		this.sockIOPool.setMinConn(5);
		this.sockIOPool.setMaxConn(250);
		this.sockIOPool.setMaxIdle( 1000 * 60 * 60 * 6);
		this.sockIOPool.setMaintSleep(30);
		this.sockIOPool.setNagle(false);
		this.sockIOPool.initialize();

		this.memCachedClient = new MemCachedClient();
		this.memCachedClient.setCompressEnable(true);
		this.memCachedClient.setCompressThreshold(0);
	}

	/* (non-Javadoc)
	 * @see org.jrest4guice.sna.CacheManager#get(java.lang.String)
	 */
	public Object get(String key) {
		return this.memCachedClient.get(key);
	}

	/* (non-Javadoc)
	 * @see org.jrest4guice.sna.CacheManager#put(java.lang.String, java.lang.Object)
	 */
	public CacheProvider put(String key, Object value) {
		this.memCachedClient.set(key, value,new Date(System.currentTimeMillis()+this.expiryTime));
		return this;
	}

	/* (non-Javadoc)
	 * @see org.jrest4guice.sna.CacheManager#delete(java.lang.String)
	 */
	public void delete(String key) {
		this.memCachedClient.delete(key);
	}

	/* (non-Javadoc)
	 * @see org.jrest4guice.sna.CacheManager#shutDown()
	 */
	public void shutDown() {
		if (this.sockIOPool != null) {
			this.sockIOPool.shutDown();
		}
	}

	/* (non-Javadoc)
	 * @see org.jrest4guice.sna.CacheProvider#getName()
	 */
	@Override
	public String getName() {
		return "memcached";
	}

	/* (non-Javadoc)
	 * @see org.jrest4guice.cache.CacheProvider#isAvailable()
	 */
	public boolean isAvailable() {
		this.memCachedClient.set(CACHE_TEST_KEY, "hello");
		return this.memCachedClient.keyExists(CACHE_TEST_KEY);
	}

	/* (non-Javadoc)
	 * @see org.jrest4guice.cache.CacheProvider#setExpiryTime(long)
	 */
	public void setExpiryTime(long time) {
		this.expiryTime = time;
	}
}
