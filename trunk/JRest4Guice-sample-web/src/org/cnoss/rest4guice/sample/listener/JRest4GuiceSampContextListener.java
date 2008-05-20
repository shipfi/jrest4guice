package org.cnoss.rest4guice.sample.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.cnoss.core.guice.GuiceContext;
import org.cnoss.core.persist.jpa.JpaGuiceModuleProvider;
import org.cnoss.core.transaction.TransactionGuiceModuleProvider;
import org.cnoss.rest4guice.JRestGuiceModuleProvider;

public class JRest4GuiceSampContextListener implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		GuiceContext.getInstance().addModuleProvider(
				new JRestGuiceModuleProvider("org.cnoss.rest4guice.sample.resources"))// JRest支持
				.addModuleProvider(new TransactionGuiceModuleProvider())// 事务支持
				.addModuleProvider(new JpaGuiceModuleProvider()).init();// JPA支持
	}
}
