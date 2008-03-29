package org.jpa4guice.transaction;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;


class EntityManagerProvider implements Provider<EntityManager> {
    private final EntityManagerFactoryHolder holder;

    @Inject
    public EntityManagerProvider(EntityManagerFactoryHolder holder) {
        this.holder = holder;
    }

    public EntityManager get() {
        return holder.getEntityManager();
    }
}
