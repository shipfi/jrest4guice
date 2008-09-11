package org.jrest4guice.sna;

import com.google.inject.Provider;

public class CacheProviderProvider implements Provider<CacheProvider> {
	private static CacheProvider provider = null;

	@Override
	public CacheProvider get() {
		return provider;
	}
	
	public static void setCurrentSecurityContext(CacheProvider _provider){
		provider = _provider;
	}
}
