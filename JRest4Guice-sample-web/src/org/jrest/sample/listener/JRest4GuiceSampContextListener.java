package org.jrest.sample.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jrest.core.guice.GuiceContext;
import org.jrest.core.persist.jpa.JpaGuiceModuleProvider;
import org.jrest.core.transaction.TransactionGuiceModuleProvider;
import org.jrest.dao.DaoModuleProvider;
import org.jrest.rest.JRestGuiceModuleProvider;

public class JRest4GuiceSampContextListener implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		GuiceContext.getInstance().addModuleProvider(
				new JRestGuiceModuleProvider("org.jrest.sample.resources"))
				.addModuleProvider(new TransactionGuiceModuleProvider())
				.addModuleProvider(new JpaGuiceModuleProvider())
				.addModuleProvider(
						new DaoModuleProvider(
								new String[] { "org.jrest.sample.dao" })).init();
	}
}
