package org.jrest.core.guice;

import com.google.inject.Module;

/**
 * Module 提供者接口<br>
 * 用于实现不同模块需要与 Guice 绑定的配置信息
 * @author <a href="mailto:gzyangfan@gmail.com">gzYangfan</a>
 */
public interface ModuleProvider {

	/**
	 * 获取 模块配置信息
	 * @return
	 */
	Module getModule();
}
