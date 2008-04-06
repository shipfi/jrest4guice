package org.jrest.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.RejectedExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jrest.dao.actions.Action;
import org.jrest.dao.actions.ActionContext;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * DAO 动态代理类<br>
 * 由该对象负责 DAO 接口的代理实例的创建，并由该对象负责拦截 DAO 接口的方法的执行，将执行操作转交为特定的<code>Action</code>类执行。
 * @author <a href="mailto:gzyangfan@gmail.com">gzYangfan</a>
 */
@Singleton
public class DynamicProxy implements InvocationHandler {

	private final static Log log = LogFactory.getLog(DynamicProxy.class);

	@Inject
	private Register register;

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

	@SuppressWarnings("unchecked")
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Action action = getAction(method);
		if (action == null) {
			String msg = "无法获取" + proxy.getClass() + "." + method.getName() + "方法声明的Action实例。";
			log.error(msg);
			throw new RejectedExecutionException(msg);
		} else {
			ActionContext context = action.getContext();
			context.setAction(action);
			return context.execute(method, args);
		}
	}

	@SuppressWarnings("unchecked")
	private Action getAction(Method method) {
		Annotation[] annotations = method.getAnnotations();
		for (Annotation annotation : annotations) {
			Class<? extends Annotation> clazz = annotation.annotationType();
			if (register.isActionAnnotation(clazz)) {
				Action action = register.createAction(annotation);
				action.setAnnotation(annotation);
				return action;
			}
		}
		return null;
	}
}
