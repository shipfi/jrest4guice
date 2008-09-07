package org.jrest4guice.transaction;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.Transaction;
import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.persistence.hibernate.SessionFactoryHolder;
import org.jrest4guice.persistence.hibernate.SessionInfo;
import org.jrest4guice.transaction.annotations.Transactional;
import org.jrest4guice.transaction.annotations.TransactionalType;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class HibernateLocalTransactionInterceptor implements MethodInterceptor {
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		SessionFactoryHolder sessionFH = GuiceContext.getInstance().getBean(SessionFactoryHolder.class);
		SessionInfo session = sessionFH.getSessionInfo();
		
		Method method = methodInvocation.getMethod();
		Transactional transactional = method.getAnnotation(Transactional.class);
		if(transactional == null){
			transactional = method.getDeclaringClass().getAnnotation(Transactional.class);
		}

		TransactionalType type = transactional.type();
		
		final Transaction transaction = session.getSession().getTransaction();

		if(type != TransactionalType.READOLNY){
			session.setNeed2ProcessTransaction(true);
		}
		
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
