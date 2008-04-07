package org.jrest.rest.http;


import com.google.inject.Provider;

public class ModelMapProvider implements Provider<ModelMap> {
	public ModelMap get() {
		return HttpContextManager.getModelMap();
	}
}
