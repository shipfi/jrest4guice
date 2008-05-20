package org.jrest4guice.context;


import com.google.inject.Provider;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
public class ModelMapProvider implements Provider<ModelMap> {
	public ModelMap get() {
		return HttpContextManager.getModelMap();
	}
}
