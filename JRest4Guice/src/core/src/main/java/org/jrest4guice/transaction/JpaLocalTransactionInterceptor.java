package org.jrest4guice.transaction;

import java.lang.reflect.Method;

import javax.persistence.EntityTransaction;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.persistence.jpa.EntityManagerFactoryHolder;
import org.jrest4guice.persistence.jpa.EntityManagerInfo;
import org.jrest4guice.transaction.annotations.Transactional;
import org.jrest4guice.transaction.annotations.TransactionalType;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class JpaLocalTransactionInterceptor implements MethodInterceptor {
	private static Log log = LogFactory.getLog(JpaLocalTransactionInterceptor.class);
	
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		log.debug("[JpaLocalTransactionInterceptor]进入＝》"+methodInvocation.getMethod().getName());

		EntityManagerFactoryHolder emfH = GuiceContext.getInstance().getBean(EntityManagerFactoryHolder.class);
		EntityManagerInfo entityManager = emfH.getEntityManagerInfo();
		
		Method method = methodInvocation.getMethod();
		Transactional transactional = method.getAnnotation(Transactional.class);
		if(transactional == null){
			transactional = method.getDeclaringClass().getAnnotation(Transactional.class);
		}
		
		TransactionalType type = transactional.type();
		if(type != TransactionalType.READOLNY){
			entityManager.setNeed2ProcessTransaction(true);
		}
		
		final EntityTransaction transaction = entityManager.getEntityManager().getTransaction();
		
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
		
		log.debug("[JpaLocalTransactionInterceptor]离开＝》"+methodInvocation.getMethod().getName());
		
		//返回业务方法的执行结果
		return result;
	}
}
