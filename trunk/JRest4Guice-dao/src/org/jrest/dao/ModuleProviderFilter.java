package org.jrest.dao;

import java.util.ArrayList;
import java.util.List;

import org.jrest.core.guice.ModuleProvider;

/**
 * 模块提供者过滤器
 * @author <a href="mailto:gzyangfan@gmail.com">gzYangfan</a>
 */
public class ModuleProviderFilter {

	private List<Class<ModuleProvider>> providerClasses = new ArrayList<Class<ModuleProvider>>();

	@SuppressWarnings("unchecked")
	public ModuleProviderFilter(List<Class<?>> classes) {
		for (Class<?> clz : classes) {
			if (ModuleProvider.class.isAssignableFrom(clz))
				providerClasses.add((Class<ModuleProvider>) clz);
		}
	}

	public List<Class<ModuleProvider>> getProviderClasses() {
		return providerClasses;
	}

}
