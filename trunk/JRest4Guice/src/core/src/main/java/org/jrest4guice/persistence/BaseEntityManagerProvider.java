package org.jrest4guice.persistence;

import java.io.Serializable;

import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.persistence.hibernate.HibernateEntityManager;
import org.jrest4guice.persistence.hibernate.SessionProvider;
import org.jrest4guice.persistence.jpa.EntityManagerProvider;
import org.jrest4guice.persistence.jpa.JpaEntityManager;

import com.google.inject.Provider;


/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
@SuppressWarnings("unchecked")
public class BaseEntityManagerProvider<PK extends Serializable, E extends EntityAble<PK>> implements Provider<BaseEntityManager> {
    public BaseEntityManager get() {
    	BaseEntityManager em = null;
    	if(GuiceContext.getInstance().isUseJPA())
    		em = new JpaEntityManager(String.class,GuiceContext.getInstance().getBean(EntityManagerProvider.class).get());
    	else if(GuiceContext.getInstance().isUseHibernate())
    		em = new HibernateEntityManager(String.class,GuiceContext.getInstance().getBean(SessionProvider.class).get());
    	
    	return em;
    }
}
