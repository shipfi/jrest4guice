package org.jrest.dao.jpa;

import org.jrest.core.guice.ModuleProvider;
import org.jrest.dao.DaoContext;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provider;

public class JpaContextProviderWithoutService implements Provider<JpaContext>, ModuleProvider {

	@Override
	public JpaContext get() {
		JpaContext context = new JpaContext();
		DaoContext.getInstance().injectorMembers(context);
		context.setWithoutService(true);
		return context;
	}

	@Override
	public Module getModule() {
		return new AbstractModule() {
			@Override
			protected void configure() {
				bind(JpaContext.class).toProvider(JpaContextProviderWithoutService.class);
			}
		};
	}
}
