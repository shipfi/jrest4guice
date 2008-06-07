package org.jrest4guice.sample.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jrest4guice.rest.JRest4GuiceHelper;

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
		.useJRest("org.jrest4guice.sample.resources")//使用Rest，并指定要动态扫描注册的包路径
		.useJPA()//使用JPA
		.useSecurity()//使用JAAS
		.init();
	}
}
