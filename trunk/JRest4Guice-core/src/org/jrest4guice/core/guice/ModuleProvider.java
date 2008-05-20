package org.jrest4guice.core.guice;

import java.util.Collection;

import com.google.inject.Module;

/**
 * 模块提供者 接口
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 */
public interface ModuleProvider {
	
	/**
	 * 设置 需要扫描的程序包
	 * @param packages 程序包
	 */
	void addScanPackages(String... packages);
	
	/**
	 * 获取 该模块的模块配置信息集合
	 * @return
	 */
	Collection<? extends Module> getModules();
}
