package org.jrest4guice.sample.contact.listener;

import javax.servlet.http.HttpSessionEvent;

import org.jrest4guice.security.SecurityContext;

public class SampleHttpSessionListener implements
		javax.servlet.http.HttpSessionListener {
	@Override
	public void sessionCreated(HttpSessionEvent event) {
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		SecurityContext.getInstance().clearUserSessionCache(event.getSession());
	}
}
