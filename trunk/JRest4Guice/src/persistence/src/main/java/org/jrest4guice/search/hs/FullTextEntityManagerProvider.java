package org.jrest4guice.search.hs;

import javax.persistence.EntityManager;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class FullTextEntityManagerProvider implements Provider<FullTextEntityManager> {
	@Inject(optional=true)
	private EntityManager entityManager;

	public FullTextEntityManager get() {
		return Search.createFullTextEntityManager(this.entityManager);
	}
}
