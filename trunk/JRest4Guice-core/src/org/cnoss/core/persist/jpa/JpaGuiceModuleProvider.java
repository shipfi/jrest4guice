package org.cnoss.core.persist.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.cnoss.core.guice.ModuleProviderTemplate;

import com.google.inject.Binder;
import com.google.inject.Module;

public class JpaGuiceModuleProvider extends ModuleProviderTemplate{
	@Override
	public List<Module> getModules() {
		List<Module> modules = new ArrayList<Module>(0);
		modules.add(new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bind(EntityManagerFactory.class).toProvider(
						EntityManagerFactoryProvider.class);
				binder.bind(EntityManager.class).toProvider(
						EntityManagerProvider.class);
			}
		});

		return modules;
	}
}
