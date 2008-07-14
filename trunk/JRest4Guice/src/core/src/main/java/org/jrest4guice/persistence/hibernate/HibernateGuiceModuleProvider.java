package org.jrest4guice.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jrest4guice.guice.ModuleProviderTemplate;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class HibernateGuiceModuleProvider extends ModuleProviderTemplate{
	@Override
	public List<Module> getModules() {
		List<Module> modules = new ArrayList<Module>(0);
		modules.add(new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bind(SessionFactory.class).toProvider(
						SessionFactoryProvider.class);
				binder.bind(Session.class).toProvider(
						SessionProvider.class);
			}
		});

		return modules;
	}
}
