package org.jrest4guice.core.security;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.jrest4guice.core.guice.ModuleProviderTemplate;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 * 
 */
public class SecurityGuiceModuleProvider extends ModuleProviderTemplate {
	@Override
	public List<Module> getModules() {
		List<Module> modules = new ArrayList<Module>(0);
		modules.add(new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bindInterceptor(Matchers.any(), Matchers
						.annotatedWith(RolesAllowed.class),
						new SecurityInterceptor());
			}
		});

		return modules;
	}
}
