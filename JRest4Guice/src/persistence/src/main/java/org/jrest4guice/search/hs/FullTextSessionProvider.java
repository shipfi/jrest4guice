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
	@Inject(optional=true)
	private Session session;

	public FullTextSession get() {
		return Search.createFullTextSession(session);
	}
}
