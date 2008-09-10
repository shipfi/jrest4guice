package org.jrest4guice.sna;

import java.util.HashMap;
import java.util.Map;

import org.jrest4guice.guice.GuiceContext;

import com.google.inject.Singleton;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@Singleton
public class CacheProviderRegister {

	private static Map<String, Class<CacheProvider>> cacheProviders = new HashMap<String, Class<CacheProvider>>(0);

	public CacheProviderRegister registCacheProvider(String name,
			Class<CacheProvider> cacheProvider) {
		cacheProviders.put(name, cacheProvider);
		return this;
	}

	public CacheProvider getCacheProvider(String name) {
		Class<CacheProvider> clazz = cacheProviders.get(name);
		if(clazz != null)
			return  GuiceContext.getInstance().getBean(clazz);
		else
			return null;
	}
	
	public static CacheProviderRegister getInstance(){
		return GuiceContext.getInstance().getBean(CacheProviderRegister.class);
	}
}
