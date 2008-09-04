package org.jrest4guice.persistence.jpa;

import javax.persistence.EntityManager;

public class EntityManagerInfo {
	private EntityManager entityManager;
	private boolean need2ProcessTransaction = false;
	
	public EntityManagerInfo(EntityManager session){
		this.entityManager = session;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	public void setEntityManager(EntityManager session) {
		this.entityManager = session;
	}
	public boolean isNeed2ProcessTransaction() {
		return need2ProcessTransaction;
	}
	public void setNeed2ProcessTransaction(boolean need2ProcessTransaction) {
		this.need2ProcessTransaction = need2ProcessTransaction;
	}
}
