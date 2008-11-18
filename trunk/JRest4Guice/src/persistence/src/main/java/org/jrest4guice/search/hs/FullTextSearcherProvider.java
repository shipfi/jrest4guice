package org.jrest4guice.search.hs;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.guice.InjectorContext;
import org.jrest4guice.guice.PersistenceGuiceContext;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import com.google.inject.Provider;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
@SuppressWarnings("unchecked")
public class FullTextSearcherProvider implements Provider<FullTextSearcher> {
	public FullTextSearcher get() {
    	Field field = InjectorContext.currentField();
    	Class<?> clazz = String.class;
    	Type genericType = field.getGenericType();
    	if(genericType instanceof ParameterizedTypeImpl){
    		ParameterizedTypeImpl pgType = (ParameterizedTypeImpl)genericType;
    		Type[] actualTypeArguments = pgType.getActualTypeArguments();
    		clazz = (Class<?>)actualTypeArguments[1];
    	}

    	if(PersistenceGuiceContext.getInstance().isUseJPA())
    		return new JpaFullTextSearcher(clazz,GuiceContext.getInstance().getBean(FullTextEntityManagerProvider.class).get());
    	else if(PersistenceGuiceContext.getInstance().isUseHibernate())
    		return null;
		return null;
	}
}
