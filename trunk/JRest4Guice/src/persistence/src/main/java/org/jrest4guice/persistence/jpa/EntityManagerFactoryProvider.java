package org.jrest4guice.persistence.jpa;

import javax.persistence.EntityManagerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;


/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class EntityManagerFactoryProvider implements Provider<EntityManagerFactory> {
    @Inject
    private EntityManagerFactoryHolder emFactoryHolder;

    public EntityManagerFactory get() {
        return this.emFactoryHolder.getEntityManagerFactory();
    }
}
