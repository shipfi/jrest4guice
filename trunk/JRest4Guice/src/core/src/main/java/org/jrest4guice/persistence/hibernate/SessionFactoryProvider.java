package org.jrest4guice.persistence.hibernate;

import org.hibernate.SessionFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;


/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class SessionFactoryProvider implements Provider<SessionFactory> {
    private final SessionFactoryHolder sessionFactoryHolder;

    @Inject
    public SessionFactoryProvider(SessionFactoryHolder sessionFactoryHolder) {
        this.sessionFactoryHolder = sessionFactoryHolder;
    }

    public SessionFactory get() {
        return this.sessionFactoryHolder.getSessionFactory();
    }
}
