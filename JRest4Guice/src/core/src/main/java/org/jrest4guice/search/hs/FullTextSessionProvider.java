package org.jrest4guice.search.hs;

import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class FullTextSessionProvider implements Provider<FullTextSession> {
	private final Session session;

	@Inject
	public FullTextSessionProvider(Session session) {
		this.session = session;
	}

	public FullTextSession get() {
		return Search.createFullTextSession(session);
	}
}
