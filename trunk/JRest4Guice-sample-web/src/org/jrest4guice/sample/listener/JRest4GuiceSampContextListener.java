package org.jrest4guice.sample.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jrest4guice.JRestGuiceModuleProvider;
import org.jrest4guice.core.guice.GuiceContext;
import org.jrest4guice.core.persist.jpa.JpaGuiceModuleProvider;
import org.jrest4guice.core.transaction.TransactionGuiceModuleProvider;

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
		GuiceContext.getInstance().addModuleProvider(
				new JRestGuiceModuleProvider("org.jrest4guice.sample.resources"))// JRest支持
				.addModuleProvider(new TransactionGuiceModuleProvider())// 事务支持
				.addModuleProvider(new JpaGuiceModuleProvider()).init();// JPA支持
	}
}
