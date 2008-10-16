package org.jrest4guice.rest.provider;

import org.apache.velocity.VelocityContext;
import org.jrest4guice.rest.RestContextManager;

import com.google.inject.Provider;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class VelocityContextProvider implements Provider<VelocityContext> {

	public VelocityContext get() {
		return RestContextManager.getVelocityContext();
	}
}
