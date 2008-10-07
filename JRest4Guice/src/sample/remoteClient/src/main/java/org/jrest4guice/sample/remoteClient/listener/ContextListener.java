package org.jrest4guice.sample.remoteClient.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jrest4guice.rest.helper.JRest4GuiceHelper;

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
		JRest4GuiceHelper.useJRest("org.jrest4guice.sample.remoteClient")// 使用Rest，并指定要动态扫描注册的包路径
				.init();
	}
}
