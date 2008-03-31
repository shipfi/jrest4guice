package org.jrest.dao.jpa;

import javax.persistence.EntityManager;

public class JpaDaoContext {
	
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}
	
}
