package org.jrest.context;


import com.google.inject.Provider;

public class ModelMapProvider implements Provider<ModelMap> {
	public ModelMap get() {
		return IocContextManager.getModelMap();
	}
}
