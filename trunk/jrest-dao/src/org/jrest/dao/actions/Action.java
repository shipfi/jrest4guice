package org.jrest.dao.actions;

import java.lang.reflect.Method;

/**
 * DAO动作 接口
 * @author <a href="mailto:gzyangfan@gmail.com">gzYangfan</a>
 * @param <A> 该动作的注解类型
 * @param <T> 执行该动作所需的上下文环境类型
 */
public interface Action<A, T> {
	
	Object execute(Method method, Object... parameters);

	void setAnnotation(A annotation);
	
	A getAnnotation();
	
	Class<A> getAnnotationClass();
	
	void setContext(T context);
	
	T getContext();
	
	Class<T> getContextClass();
	
}
