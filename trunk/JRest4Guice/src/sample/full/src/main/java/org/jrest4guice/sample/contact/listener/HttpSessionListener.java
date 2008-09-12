package org.jrest4guice.sample.contact.listener;

import javax.servlet.http.HttpSessionEvent;

import org.jrest4guice.security.SecurityContext;

public class HttpSessionListener implements
		javax.servlet.http.HttpSessionListener {
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		SecurityContext.getInstance().clearUserPrincipalCache();
	}
}
