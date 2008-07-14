package org.jrest4guice.transaction;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.persistence.hibernate.SessionFactoryHolder;
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
		Session session = sessionFH.getSession();
		
		Transactional transactional = methodInvocation.getMethod().getAnnotation(Transactional.class);
		TransactionalType type = transactional.type();
		
		final Transaction transaction = session.beginTransaction();
		
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
