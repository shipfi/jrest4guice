package org.jrest4guice.persistence.jpa;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class EntityManagerProvider implements Provider<EntityManager> {
	@Inject
	private EntityManagerFactoryHolder holder;

	public EntityManager get() {
		return holder.getEntityManagerInfo().getEntityManager();
	}
}
