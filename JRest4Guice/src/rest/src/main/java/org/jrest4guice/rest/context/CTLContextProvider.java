package org.jrest4guice.rest.context;

import org.commontemplate.core.Context;

import com.google.inject.Provider;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class CTLContextProvider implements Provider<Context> {

	public Context get() {
		return RestContextManager.getCTLContext();
	}
}
