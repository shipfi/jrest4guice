package org.jpa4guice.transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.google.inject.Singleton;

@Singleton
public class EntityManagerFactoryHolder {
	private EntityManagerFactory entityManagerFactory;

	private final ThreadLocal<EntityManager> entityManager = new ThreadLocal<EntityManager>();

	public EntityManagerFactoryHolder() {
		this.entityManagerFactory = Persistence
				.createEntityManagerFactory("JPA4Guice");
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return this.entityManagerFactory;
	}

	public EntityManager getEntityManager() {
		EntityManager em = this.entityManager.get();
		// 如果不存在，则创建一个新的
		if (em == null) {
			em = getEntityManagerFactory().createEntityManager();
			this.entityManager.set(em);
		}
		return em;
	}

	public void closeEntityManager() {
		EntityManager em = this.entityManager.get();
		if (em != null) {
			try {
				if (em.isOpen())
					em.close();
			} finally {
				this.entityManager.remove();
			}
		}
	}
}
