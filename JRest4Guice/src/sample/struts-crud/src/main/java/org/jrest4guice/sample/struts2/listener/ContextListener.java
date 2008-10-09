package org.jrest4guice.sample.struts2.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jrest4guice.guice.PersistenceGuiceContext;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
public class ContextListener implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		PersistenceGuiceContext.getInstance().useJPA().init();
	}
}
