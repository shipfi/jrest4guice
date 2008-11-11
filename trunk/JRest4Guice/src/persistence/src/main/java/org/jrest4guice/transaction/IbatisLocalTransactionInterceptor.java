package org.jrest4guice.transaction;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.persistence.ibatis.SqlMapClientHolder;
import org.jrest4guice.transaction.annotations.Transactional;
import org.jrest4guice.transaction.annotations.TransactionalType;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class IbatisLocalTransactionInterceptor implements MethodInterceptor {
	private static Log log = LogFactory.getLog(IbatisLocalTransactionInterceptor.class);
	
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		log.debug("[IbatisLocalTransactionInterceptor]进入＝》"+methodInvocation.getMethod().getName());
		SqlMapClientHolder sqlMapClientHolder = GuiceContext.getInstance().getBean(SqlMapClientHolder.class);
		
		Method method = methodInvocation.getMethod();
		Transactional transactional = method.getAnnotation(Transactional.class);
		if(transactional == null){
			transactional = method.getDeclaringClass().getAnnotation(Transactional.class);
		}
		
		TransactionalType type = transactional.type();
		
		IbatisTransaction transaction = sqlMapClientHolder.getIbatisTransaction();
		
		if(transaction.isActive()){
			return methodInvocation.proceed();
		}

		//开始一个新的事务
		if(type != TransactionalType.READOLNY){
			transaction.begin();
		}
		
		Object result = null;
		try {
			//执行被拦截的业务方法
			result = methodInvocation.proceed();
			//提交事务
			if(type != TransactionalType.READOLNY && transaction.isActive()){
				transaction.commit();
			}
		} catch (Exception e) {
			throw e;
		}
		
		log.debug("[IbatisLocalTransactionInterceptor]离开＝》"+methodInvocation.getMethod().getName());
		
		//返回业务方法的执行结果
		return result;
	}
}
