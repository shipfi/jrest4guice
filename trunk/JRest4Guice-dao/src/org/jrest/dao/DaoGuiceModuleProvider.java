package org.jrest.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jrest.core.guice.AbstractGuiceModuleProvider;
import org.jrest.dao.annotations.Dao;

import com.google.inject.Binder;
import com.google.inject.Module;

@SuppressWarnings("unchecked")
public class DaoGuiceModuleProvider extends AbstractGuiceModuleProvider{
	public DaoGuiceModuleProvider(){
		this(null);
	}
	public DaoGuiceModuleProvider(Set<String> packageList){
		this.setScanPackageList(packageList);
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
