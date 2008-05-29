package org.jrest4guice.core.jndi;

import java.util.ArrayList;
import java.util.List;

import org.jrest4guice.core.guice.ModuleProviderTemplate;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 * 
 */
public class JndiGuiceModuleProvider extends ModuleProviderTemplate {
	@Override
	public List<Module> getModules() {
		List<Module> modules = new ArrayList<Module>(0);
		modules.add(new Module() {
			@Override
			public void configure(Binder binder) {
				for (Class<?> clazz : classes) {
				}			
			}
		});

		return modules;
	}
}
