package org.jrest.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.jrest.core.guice.GuiceModuleProvider;
import org.jrest.dao.annotations.Transactional;
import org.jrest.dao.jpa.interceptor.JpaLocalTransactionInterceptor;
import org.jrest.dao.jpa.transaction.EntityManagerFactoryProvider;
import org.jrest.dao.jpa.transaction.EntityManagerProvider;

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
						new JpaLocalTransactionInterceptor());

				binder.bind(EntityManagerFactory.class).toProvider(
						EntityManagerFactoryProvider.class);
				binder.bind(EntityManager.class).toProvider(
						EntityManagerProvider.class);
			}
		});

		return modules;
	}
}
