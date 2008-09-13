package org.jrest4guice.rest.context;

import org.commontemplate.engine.Engine;

import com.google.inject.Provider;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class CTLEngineProvider implements Provider<Engine> {

	public Engine get() {
		return RestContextManager.ctlEngine;
	}
}
