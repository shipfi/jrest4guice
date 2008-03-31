package org.jrest.dao;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jrest.dao.actions.Action;

public abstract class AbstractRegister implements Register {

	private static final Log log = LogFactory.getLog(AbstractRegister.class);
	
	@SuppressWarnings("unchecked")
	private HashMap<Class<? extends Annotation>, Class<? extends Action>> actions =
		new HashMap<Class<? extends Annotation>, Class<? extends Action>>();
	private HashSet<Class<?>> contexts = new HashSet<Class<?>>();

	public AbstractRegister() {
		this.initialize();
	}
	
	abstract protected void initialize();

	@SuppressWarnings("unchecked")
	@Override
	public Action createAction(Annotation annotation) {
		Class<? extends Annotation> clazz = annotation.annotationType();
		if (isActionAnnotation(clazz)) {
			Class<? extends Action> target = this.actions.get(clazz);
			try {
				Action action = target.getConstructor().newInstance();
				return action;
			} catch (Exception e) {
				log.error("无法创建 Action 实例", e.getCause());
			}
		}
		return null;
	}

	@Override
	public boolean isActionAnnotation(Class<? extends Annotation> clazz) {
		if (this.actions.containsKey(clazz))
			return true;
		return false;
	}

	@Override
	public boolean isContextClass(Class<?> clazz) {
		if (this.contexts.contains(clazz))
			return true;
		return false;
	}

	@Override
	public void register(Class<?> contextClass) {
		this.contexts.add(contextClass);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void register(Class<? extends Annotation> annotation, Class<? extends Action> action) {
		this.actions.put(annotation, action);
	}

}
