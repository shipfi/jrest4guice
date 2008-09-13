package org.jrest4guice.cache;

import java.util.ArrayList;
import java.util.List;

import org.jrest4guice.guice.ModuleProviderTemplate;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@SuppressWarnings("unchecked")
public class CacheGuiceModuleProvider extends ModuleProviderTemplate {
	
	public CacheGuiceModuleProvider(String... packages){
		super(packages);
		this.addScanPackages("org.jrest4guice.cache");
	}
	
	@Override
	public List<Module> getModules() {
		List<Module> modules = new ArrayList<Module>(0);
		modules.add(new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bind(CacheProvider.class).toProvider(CacheProviderProvider.class);
				CacheProviderRegister cacheManagerRegister = new CacheProviderRegister();
				for (Class clazz : classes) {
					if(CacheProvider.class.isAssignableFrom(clazz)){
						try {
							cacheManagerRegister.registCacheProvider(((CacheProvider)clazz.newInstance()).getName(), clazz);
						} catch (Exception e) {
						}
					}
				}
			}
		});
		return modules;
	}
}
