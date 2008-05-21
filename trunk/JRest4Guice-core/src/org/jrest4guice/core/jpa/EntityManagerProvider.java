package org.jrest4guice.core.jpa;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
public class EntityManagerProvider implements Provider<EntityManager> {
	private final EntityManagerFactoryHolder holder;

	@Inject
	public EntityManagerProvider(EntityManagerFactoryHolder holder) {
		this.holder = holder;
	}

	public EntityManager get() {
		return holder.getEntityManager();
	}
}
