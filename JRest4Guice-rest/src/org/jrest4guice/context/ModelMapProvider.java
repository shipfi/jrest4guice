package org.jrest4guice.context;


import com.google.inject.Provider;

public class ModelMapProvider implements Provider<ModelMap> {
	public ModelMap get() {
		return HttpContextManager.getModelMap();
	}
}
