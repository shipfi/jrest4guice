package org.jrest.dao.jpa;

import javax.persistence.EntityManager;

import com.google.inject.Inject;

public class JpaDaoContext {
	
	@Inject
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}
	
}
