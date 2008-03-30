package org.jpa4guice.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jpa4guice.annotation.DaoMethod;
import org.jpa4guice.annotation.DaoMethodType;
import org.jpa4guice.annotation.FirstResult;
import org.jpa4guice.annotation.MaxResults;

import com.google.inject.Inject;
import com.google.inject.cglib.proxy.Enhancer;
import com.google.inject.cglib.proxy.MethodInterceptor;
import com.google.inject.cglib.proxy.MethodProxy;
import com.google.inject.name.Named;

public class DaoCglibProxy implements MethodInterceptor {
	private Enhancer enhancer = new Enhancer();

	@Inject
	private EntityManager entityManager;

	public <T> T getProxy(Class<T> clazz) {
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(this);
		return (T) enhancer.create();
	}

	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		Object result = null;
		if(method.isAnnotationPresent(DaoMethod.class)){
			//System.out.println(this.entityManager);
			
			DaoMethod annotation = method.getAnnotation(DaoMethod.class);
			DaoMethodType type = annotation.type();
			switch (type) {
			case PERSIST:
				this.entityManager.persist(args[0]);
				break;
			case FIND:
				result = this.entityManager.find(method.getReturnType(), args[0]);
				break;
			case MERGE:
				this.entityManager.merge(args[0]);
				break;
			case REMOVE:
				this.entityManager.remove(args[0]);
				break;
			case LIST:
				result = this.doQuery(method, annotation, args);
				break;
			default:
				break;
			}
		}

		return result;
	}
	
	private Object doQuery(Method method,DaoMethod annotation,Object[] args){
		Object result = null;

		Annotation[][] annotationArray = method
		.getParameterAnnotations();
		
		Query query = null;
		
		if(!annotation.query().trim().equals(""))
			query = this.entityManager.createQuery(annotation.query());
		else if(!annotation.namedQuery().trim().equals("")){
			query = this.entityManager.createNamedQuery(annotation.namedQuery());
		}
		
		if(query == null)
			throw new RuntimeException(method.getName()+" 执行失败，原因：没有为查询指定EQL语句或者是没有指定命名查询的名称");

		
		int valueIndex = 0;

		String pName;
		int paramIndex = 1;
		for (Annotation[] annotations : annotationArray) {
			for (Annotation paramAnnotation : annotations) {
				if (paramAnnotation instanceof FirstResult) {
					query.setFirstResult(Integer.parseInt(args[valueIndex].toString()));
				} else if (paramAnnotation instanceof MaxResults) {
					query.setMaxResults(Integer.parseInt(args[valueIndex].toString()));
				} else if (paramAnnotation instanceof Named) {
					pName = ((Named)paramAnnotation).value();
					query.setParameter(pName, args[valueIndex]);
				}else{
					query.setParameter(paramIndex, args[valueIndex]);
					paramIndex ++;
				}
			}
			valueIndex++;
		}
		
		result = query.getResultList();
		
		return result;
	}
}
