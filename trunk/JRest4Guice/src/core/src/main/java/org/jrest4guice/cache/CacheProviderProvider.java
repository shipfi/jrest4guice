package org.jrest4guice.cache;

import com.google.inject.Provider;

public class CacheProviderProvider implements Provider<CacheProvider> {
	private static CacheProvider provider = null;

	@Override
	public CacheProvider get() {
		return provider;
	}
	
	public static void setCurrentCacheProvider(CacheProvider _provider){
		provider = _provider;
	}
}
