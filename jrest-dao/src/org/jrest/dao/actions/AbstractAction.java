package org.jrest.dao.actions;

import java.lang.reflect.Method;

public abstract class AbstractAction<A, T> implements Action<A, T> {

	protected A annotation;
	protected T context;
	protected Method method;
	
	protected Class<T> contextClass;
	protected Class<A> annotationClass;
	
	public AbstractAction() {
		this.initialize();
	}
	
	@Override
	public A getAnnotation() {
		return this.annotation;
	}

	@Override
	public T getContext() {
		return this.context;
	}

	@Override
	public void setAnnotation(A annotation) {
		this.annotation = annotation;
	}

	@Override
	public Class<A> getAnnotationClass() {
		return this.annotationClass;
	}

	@Override
	public void setContext(T context) {
		this.context = context;
	}

	@Override
	public Object execute(Method method, Object... parameters) {
		this.method = method;
		return execute(parameters);
	}

	public Class<?> getMethodReturnType() {
		return this.method.getReturnType();
	}
	
	public Class<T> getContextClass() {
		return contextClass;
	}
	
	/**
	 * 初始化方法
	 */
	abstract protected void initialize();
	
	abstract public Object execute(Object... parameters);
	
}
