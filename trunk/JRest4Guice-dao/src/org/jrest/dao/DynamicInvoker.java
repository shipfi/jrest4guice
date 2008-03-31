package org.jrest.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.jrest.dao.actions.Action;

/**
 * 
 * @author <a href="mailto:gzyangfan@gmail.com">gzYangfan</a>
 */
public class DynamicInvoker implements InvocationHandler {
	
	private Register register;
	
	public void setRegister(Register register) {
		this.register = register;
	}

	@SuppressWarnings("unchecked")
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		//? action 反复创建的问题
		Action action = getAction(method);
		if (action != null) {
			return action.execute(method, args);
		}
		return null;
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
