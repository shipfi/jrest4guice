package org.jrest4guice.rest;

import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.jndi.JndiGuiceModuleProvider;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 * 
 */
public class JRest4GuiceHelper {
	public static GuiceContext useJRest(String... scanPaths) {
		return GuiceContext.getInstance().addModuleProvider(
				new JRestGuiceModuleProvider(scanPaths)).addModuleProvider(
				new JndiGuiceModuleProvider(scanPaths));
	}
}
