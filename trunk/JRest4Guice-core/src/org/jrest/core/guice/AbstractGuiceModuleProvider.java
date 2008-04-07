package org.jrest.core.guice;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jrest.core.util.ClassUtils;

import com.google.inject.Module;

public abstract class AbstractGuiceModuleProvider implements GuiceModuleProvider {

	protected Set<Class<?>> classes;

	@Override
	public abstract List<Module> getModules();

	@Override
	public void setScanPackageList(Set<String> packageList) {
		this.classes = new HashSet<Class<?>>(0);
		if(packageList != null){
			for(String packageName :packageList){
				classes.addAll(ClassUtils.getClasses(packageName));
			}
		}
	}
}