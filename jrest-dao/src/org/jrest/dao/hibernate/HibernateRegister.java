package org.jrest.dao.hibernate;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jrest.dao.Register;
import org.jrest.dao.actions.Action;
import org.jrest.dao.annotations.Create;
import org.jrest.dao.annotations.Delete;
import org.jrest.dao.annotations.Find;
import org.jrest.dao.annotations.Retrieve;
import org.jrest.dao.annotations.Update;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class HibernateRegister extends HibernateDaoSupport implements Register {
	
	private static final Log log = LogFactory.getLog(HibernateRegister.class);
	
	@SuppressWarnings("unchecked")
	private HashMap<Class<? extends Annotation>, Class<? extends Action>> actions =
		new HashMap<Class<? extends Annotation>, Class<? extends Action>>();
	private HashSet<Class<?>> contexts = new HashSet<Class<?>>();

	public HibernateRegister() {
		// TODO 需要改为通过Guice注入内容
		register(Create.class, CreateAction.class);
		register(Delete.class, DeleteAction.class);
		register(Retrieve.class, RetrieveAction.class);
		register(Update.class, UpdateAction.class);
		register(Find.class, FindAction.class);
		register(HibernateDaoContext.class);
	}
	
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
	public Object createContext(Class<?> clazz) {
		// TODO 需要改为通过Guice获取对象
		if (clazz.equals(HibernateDaoContext.class)) {
			HibernateDaoContext context = new HibernateDaoContext();
			context.setSession(this.getSession(true));
			return context;
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
