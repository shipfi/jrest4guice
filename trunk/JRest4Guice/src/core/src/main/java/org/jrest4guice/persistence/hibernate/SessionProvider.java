package org.jrest4guice.persistence.hibernate;

import org.hibernate.Session;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class SessionProvider implements Provider<Session> {
	private final SessionFactoryHolder holder;

	@Inject
	public SessionProvider(SessionFactoryHolder holder) {
		this.holder = holder;
	}

	public Session get() {
		return holder.getSession();
	}
}
