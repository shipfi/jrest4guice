package org.jrest.dao.actions;

import java.lang.annotation.Annotation;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jrest.core.guice.GuiceContext;

import com.google.inject.Singleton;

/**
 * 动作注册器<br>
 * 该对象负责登记可用的<code>Action</code>对象，并由该对象负责创建<code>Action</code>实例
 * @author <a href="mailto:gzyangfan@gmail.com">gzYangfan</a>
 */
@Singleton
public final class ActionRegister {
	
	private static final Log log = LogFactory.getLog(ActionRegister.class);
	
	@SuppressWarnings("unchecked")
	private HashMap<Class<? extends Annotation>, Class<? extends Action>> actions = new HashMap<Class<? extends Annotation>, Class<? extends Action>>();
	
	@SuppressWarnings("unchecked")
	public Action createAction(Annotation annotation) {
		Class<? extends Annotation> clazz = annotation.annotationType();
		if (isActionAnnotation(clazz)) {
			Class<? extends Action> target = actions.get(clazz);
			return GuiceContext.getInstance().getBean(target);
		}
		return null;
	}
	
	public boolean isActionAnnotation(Class<? extends Annotation> clazz) {
		if (actions.containsKey(clazz))
			return true;
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public void register(Class<Action> clz) {
		try {
			Action action = clz.newInstance();
			actions.put(action.getAnnotationClass(), clz);
		} catch (Exception e) {
			// 不应该出现的错误
			log.error("无法实例化 Action 类:" + clz.getName(), e);
		}
	}
	
}