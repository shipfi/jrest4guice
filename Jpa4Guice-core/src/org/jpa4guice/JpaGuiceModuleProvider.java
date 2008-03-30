package org.jpa4guice;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.cnoss.guice.GuiceModuleProvider;
import org.jpa4guice.annotation.Transactional;
import org.jpa4guice.interceptor.LocalTransactionInterceptor;
import org.jpa4guice.transaction.EntityManagerFactoryProvider;
import org.jpa4guice.transaction.EntityManagerProvider;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;

public class JpaGuiceModuleProvider implements GuiceModuleProvider {
	@Override
	public List<Module> getModules() {
		List<Module> modules = new ArrayList<Module>(0);
		modules.add(new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bindInterceptor(Matchers.any(), Matchers
						.annotatedWith(Transactional.class),
						new LocalTransactionInterceptor());

				binder.bind(EntityManagerFactory.class).toProvider(
						EntityManagerFactoryProvider.class);
				binder.bind(EntityManager.class).toProvider(
						EntityManagerProvider.class);
			}
		});

		return modules;
	}
}
