package org.jrest.dao.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.jrest.core.guice.ModuleProvider;
import org.jrest.core.persist.jpa.EntityManagerFactoryProvider;
import org.jrest.core.persist.jpa.EntityManagerProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

public class JpaModuleProvider implements ModuleProvider {

	class JpaModule extends AbstractModule {
		@Override
		protected void configure() {
			bind(EntityManagerFactory.class).toProvider(EntityManagerFactoryProvider.class);
			bind(EntityManager.class).toProvider(EntityManagerProvider.class);
		}
	}

	@Override
	public Module getModule() {
		return new JpaModule();
	}
}
