package org.jrest4guice.rest.provider;


import org.jrest4guice.client.ModelMap;
import org.jrest4guice.rest.RestContextManager;

import com.google.inject.Provider;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class ModelMapProvider implements Provider<ModelMap> {
	public ModelMap get() {
		return RestContextManager.getModelMap();
	}
}
