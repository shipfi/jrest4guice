package org.jrest4guice.persistence.ibatis;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Singleton;

/**
 * Ibatis DAO 动态代理类<br>
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 */
@Singleton
@SuppressWarnings("unchecked")
public class IbatisDaoDynamicProxy implements InvocationHandler {

	private final static Log log = LogFactory.getLog(IbatisDaoDynamicProxy.class);

	/**
	 * 创建指定 DAO 接口的代理实现类
	 * @param <T> DAO接口类型
	 * @param daoClazz DAO接口的 class
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T createDao(Class<T> daoClazz) {
		return (T) Proxy.newProxyInstance(daoClazz.getClassLoader(), new Class[] { daoClazz }, this);
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return method.invoke(proxy, args);
	}
}
