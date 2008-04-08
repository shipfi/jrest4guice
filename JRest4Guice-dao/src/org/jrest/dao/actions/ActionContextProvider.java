package org.jrest.dao.actions;

import com.google.inject.Module;
import com.google.inject.Provider;

/**
 * <code>ActionContext</code> 提供者接口
 * @author <a href="mailto:gzyangfan@gmail.com">gzYangfan</a>
 */
public interface ActionContextProvider<T extends ActionContext> extends Provider<T> {
	
	/**
	 * 获取<code>ActionContext</code>模块配置信息
	 * @return
	 */
	Module getModule();
}
