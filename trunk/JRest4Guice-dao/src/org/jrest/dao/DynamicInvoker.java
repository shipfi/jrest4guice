package org.jrest.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.RejectedExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jrest.dao.actions.Action;

/**
 * 
 * @author <a href="mailto:gzyangfan@gmail.com">gzYangfan</a>
 */
public class DynamicInvoker implements InvocationHandler {
	
	private final static Log log = LogFactory.getLog(DynamicInvoker.class);
	
	private Register register;
	
	public void setRegister(Register register) {
		this.register = register;
	}

	@SuppressWarnings("unchecked")
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Action action = getAction(method);
		if (action == null) {
			String msg = "无法获取" + proxy.getClass() + "." + method.getName() + "方法声明的Action实例。";
			log.error(msg);
			throw new RejectedExecutionException(msg);
		} else {
			return action.execute(method, args);
		}
	}

	@SuppressWarnings("unchecked")
	private Action getAction(Method method) {
		Annotation[] annotations = method.getAnnotations();
		for (Annotation annotation : annotations) {
			Class<? extends Annotation> clazz = annotation.annotationType();
			if (this.register.isActionAnnotation(clazz)) {
				Action action = this.register.createAction(annotation);
				action.setAnnotation(annotation);
				action.setContext(this.register.createContext(action.getContextClass()));
				return action;
			}
		}
		return null;
	}

}
