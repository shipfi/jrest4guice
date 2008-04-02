package org.jrest.core.persist.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.jrest.core.guice.GuiceModuleProvider;

import com.google.inject.Binder;
import com.google.inject.Module;

public class JpaGuiceModuleProvider implements GuiceModuleProvider {
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
