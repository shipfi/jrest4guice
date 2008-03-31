package org.jrest.dao;

import java.lang.annotation.Annotation;

import org.jrest.dao.actions.Action;

public interface Register {
	
	Object createContext(Class<?> clazz);
	
	boolean isContextClass(Class<?> clazz);

	void register(Class<?> contextClass);

	@SuppressWarnings("unchecked")
	Action createAction(Annotation annotation);

	boolean isActionAnnotation(Class<? extends Annotation> clazz);
	
	@SuppressWarnings("unchecked")
	void register(Class<? extends Annotation> annotation, Class<? extends Action> action);
	
}
