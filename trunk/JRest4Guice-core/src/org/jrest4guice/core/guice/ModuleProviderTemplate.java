package org.jrest4guice.core.guice;

import java.util.HashSet;
import java.util.Set;

import org.jrest4guice.core.util.ClassUtils;

/**
 * 模块提供者 模板类<br>
 * 提供一个<code>ModuleProvider</code>的简单模板，用于简化<code>ModuleProvider</code>实现类的开发。
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 */
public abstract class ModuleProviderTemplate implements ModuleProvider {
	
	protected Set<Class<?>> classes = new HashSet<Class<?>>();
	
	public ModuleProviderTemplate(String... packages) {
		addScanPackages(packages);
	}
	
	@Override
	public void addScanPackages(String... packages) {
		for (String name : packages) {
			classes.addAll(ClassUtils.getClasses(name));
		}
	}
}