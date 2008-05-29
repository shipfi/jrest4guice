package org.jrest4guice;

import org.jrest4guice.core.guice.GuiceContext;
import org.jrest4guice.core.jndi.JndiGuiceModuleProvider;

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
