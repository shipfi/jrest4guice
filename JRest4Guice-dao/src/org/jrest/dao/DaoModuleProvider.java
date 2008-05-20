package org.jrest.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cnoss.core.guice.ModuleProviderTemplate;
import org.jrest.dao.actions.Action;
import org.jrest.dao.actions.ActionContext;
import org.jrest.dao.actions.ActionContextProvider;
import org.jrest.dao.actions.ActionRegister;
import org.jrest.dao.annotations.Dao;

import com.google.inject.Binder;
import com.google.inject.Module;

public class DaoModuleProvider extends ModuleProviderTemplate {
	
	private Collection<ActionContextProvider<? extends ActionContext>> actionContextProviders = new ArrayList<ActionContextProvider<? extends ActionContext>>();
	
	public DaoModuleProvider(String... packages) {
		super(packages);
	}
	
	@Override
	public List<Module> getModules() {
		List<Module> modules = new ArrayList<Module>(0);
		modules.add(new Module() {
			@Override
			@SuppressWarnings("unchecked")
			public void configure(Binder binder) {
				ActionRegister register = new ActionRegister();
				for (Class clazz : classes) {
					if (clazz.isAnnotationPresent(Dao.class)) {
						binder.bind(clazz).toProvider(DaoProvider.create(clazz));
					} else if (Action.class.isAssignableFrom(clazz)) {
						register.register(clazz);
					}
				}
				binder.bind(ActionRegister.class).toInstance(register);
			}
		});
		for (ActionContextProvider<? extends ActionContext> provider : actionContextProviders) {
			modules.add(provider.getModule());
		}
		return modules;
	}
	
	public DaoModuleProvider addActionContextProviders(ActionContextProvider<? extends ActionContext>... providers) {
		for (ActionContextProvider<? extends ActionContext> provider : providers) {
			actionContextProviders.add(provider);
		}
		return this;
	}
}
