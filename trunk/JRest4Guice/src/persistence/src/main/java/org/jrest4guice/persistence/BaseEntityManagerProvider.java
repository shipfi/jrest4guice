package org.jrest4guice.persistence;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.guice.InjectorContext;
import org.jrest4guice.guice.PersistenceGuiceContext;
import org.jrest4guice.persistence.hibernate.HibernateEntityManager;
import org.jrest4guice.persistence.hibernate.SessionProvider;
import org.jrest4guice.persistence.jpa.EntityManagerProvider;
import org.jrest4guice.persistence.jpa.JpaEntityManager;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import com.google.inject.Provider;


/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
@SuppressWarnings("unchecked")
public class BaseEntityManagerProvider implements Provider<BaseEntityManager> {
    public BaseEntityManager get() {
    	Field field = InjectorContext.currentField();
    	Class<?> clazz = String.class;
    	Type genericType = field.getGenericType();
    	if(genericType instanceof ParameterizedTypeImpl){
    		ParameterizedTypeImpl pgType = (ParameterizedTypeImpl)genericType;
    		Type[] actualTypeArguments = pgType.getActualTypeArguments();
    		clazz = (Class<?>)actualTypeArguments[1];
    	}
    	
//		System.out.println("泛行的类型："+clazz.getName());

		BaseEntityManager em = null;
    	if(PersistenceGuiceContext.getInstance().isUseJPA())
    		em = new JpaEntityManager(clazz,GuiceContext.getInstance().getBean(EntityManagerProvider.class).get());
    	else if(PersistenceGuiceContext.getInstance().isUseHibernate())
    		em = new HibernateEntityManager(clazz,GuiceContext.getInstance().getBean(SessionProvider.class).get());
    	
    	return em;
    }
}
