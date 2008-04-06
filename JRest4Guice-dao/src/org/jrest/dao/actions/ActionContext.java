package org.jrest.dao.actions;

import java.lang.reflect.Method;

/**
 * @author <a href="mailto:gzyangfan@gmail.com">gzYangfan</a>
 */
public interface ActionContext {

	void setAction(Action action);

	Object execute(Method method, Object[] parameters) throws Throwable;
}
