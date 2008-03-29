/**
 * Copyright (C) 2008 Wideplay Interactive.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jpa4guice.transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
