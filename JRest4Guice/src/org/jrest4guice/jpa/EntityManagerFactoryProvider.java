package org.jrest4guice.jpa;

import javax.persistence.EntityManagerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;


/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
public class EntityManagerFactoryProvider implements Provider<EntityManagerFactory> {
    private final EntityManagerFactoryHolder emFactoryHolder;

    @Inject
    public EntityManagerFactoryProvider(EntityManagerFactoryHolder sessionFactoryHolder) {
        this.emFactoryHolder = sessionFactoryHolder;
    }

    public EntityManagerFactory get() {
        return this.emFactoryHolder.getEntityManagerFactory();
    }
}
