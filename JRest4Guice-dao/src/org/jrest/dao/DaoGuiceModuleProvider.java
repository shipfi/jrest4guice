package org.jrest.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.jrest.core.guice.AbstractGuiceModuleProvider;
import org.jrest.dao.annotations.Dao;

import com.google.inject.Binder;
import com.google.inject.Module;

@SuppressWarnings("unchecked")
public class DaoGuiceModuleProvider extends AbstractGuiceModuleProvider {
	public DaoGuiceModuleProvider() {
		this(null);
	}

	public DaoGuiceModuleProvider(String[] packages) {
		if (packages != null)
			this
					.setScanPackageList(new HashSet<String>(Arrays
							.asList(packages)));
		else
			this.setScanPackageList(null);
	}

	@Override
	public List<Module> getModules() {
		List<Module> modules = new ArrayList<Module>(0);
		modules.add(new Module() {
			@Override
			public void configure(Binder binder) {
				for (Class clazz : classes) {
					if (clazz.isAnnotationPresent(Dao.class)) {
						binder.bind(clazz)
								.toProvider(DaoProvider.create(clazz));
					}
				}
			}
		});
		return modules;
	}
}
