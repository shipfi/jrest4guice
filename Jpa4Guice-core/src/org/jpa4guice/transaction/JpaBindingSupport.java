package org.jpa4guice.transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.google.inject.Binder;

public class JpaBindingSupport {
	public static void addBindings(Binder binder){
        binder.bind(EntityManagerFactory.class).toProvider(EntityManagerFactoryProvider.class);
        binder.bind(EntityManager.class).toProvider(EntityManagerProvider.class);
	}
}
