package org.cnoss.core.persist.jpa;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

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
