package org.jrest4guice.dao.actions;

import java.lang.reflect.Method;

/**
 * 动作上下文接口
 * @author <a href="mailto:gzyangfan@gmail.com">gzYangfan</a>
 */
public interface ActionContext {
	
	/**
	 * 设置当前上下文要执行的动作实例
	 * @param action 具体的动作实例
	 */
	@SuppressWarnings("unchecked")
	void setAction(Action action);
	
	/**
	 * 动作的执行方法
	 * @param method DAO接口的方法对象
	 * @param parameters 参数数组
	 * @return
	 * @throws Throwable
	 */
	Object execute(Method method, Object[] parameters) throws Throwable;
}
