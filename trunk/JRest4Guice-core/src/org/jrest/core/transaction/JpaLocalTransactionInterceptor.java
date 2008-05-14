package org.jrest.core.transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jrest.core.guice.GuiceContext;
import org.jrest.core.persist.jpa.EntityManagerFactoryHolder;
import org.jrest.core.transaction.annotations.Transactional;
import org.jrest.core.transaction.annotations.TransactionalType;

public class JpaLocalTransactionInterceptor implements MethodInterceptor {
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		EntityManagerFactoryHolder emfH = GuiceContext.getInstance().getBean(EntityManagerFactoryHolder.class);
		EntityManager entityManager = emfH.getEntityManager();
		
		Transactional transactional = methodInvocation.getMethod().getAnnotation(Transactional.class);
		TransactionalType type = transactional.type();
		
//		System.out.println("current method : "+methodInvocation.getMethod());
//		System.out.println("current entityManager : "+entityManager);
		
		final EntityTransaction transaction = entityManager.getTransaction();
		
		if(transaction.isActive()){
			return methodInvocation.proceed();
		}

		//开始一个新的事务
		transaction.begin();
		
		Object result = null;
		try {
			//执行被拦截的业务方法
			result = methodInvocation.proceed();
			//提交事务
			if(type != TransactionalType.READOLNY){
				transaction.commit();
			}
		} catch (Exception e) {
			//回滚当前事务
			if(type != TransactionalType.READOLNY){
				transaction.rollback();
			}
			throw e;
		}
		
		//关闭实体管理器
		emfH.closeEntityManager();
		
		//返回业务方法的执行结果
		return result;
	}
}
