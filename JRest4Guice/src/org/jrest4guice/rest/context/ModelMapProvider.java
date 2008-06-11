package org.jrest4guice.rest.context;


import org.jrest4guice.client.ModelMap;

import com.google.inject.Provider;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ：86895156)</a>
 *
 */
public class ModelMapProvider implements Provider<ModelMap> {
	public ModelMap get() {
		return HttpContextManager.getModelMap();
	}
}
