package org.jrest4guice.transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.jpa.EntityManagerFactoryHolder;
import org.jrest4guice.transaction.annotations.Transactional;
import org.jrest4guice.transaction.annotations.TransactionalType;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
public class JpaLocalTransactionInterceptor implements MethodInterceptor {
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		EntityManagerFactoryHolder emfH = GuiceContext.getInstance().getBean(EntityManagerFactoryHolder.class);
		EntityManager entityManager = emfH.getEntityManager();
		
		Transactional transactional = methodInvocation.getMethod().getAnnotation(Transactional.class);
		TransactionalType type = transactional.type();
		
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
		
		//返回业务方法的执行结果
		return result;
	}
}
