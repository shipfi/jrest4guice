package org.jrest4guice.sample.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jrest4guice.JRest4GuiceHelper;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 * 
 */
public class JRest4GuiceSampContextListener implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		JRest4GuiceHelper
		.useJRest("org.jrest4guice.sample.resources")
		.useJPA()
//		.useSecurity()
		.init();
	}
}
