package org.jrest.core.guice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jrest.core.util.Assert;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * 全局上下文对象实体
 */
public class GuiceContext {
	
	private static volatile GuiceContext me;
	
	private volatile boolean initialized = false;
	
	private final Set<ModuleProvider> providers; // 模块提供者集合
	
	private Injector injector;
	
	private GuiceContext() {
		providers = new HashSet<ModuleProvider>();
	}
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static GuiceContext getInstance() {
		if (me == null)
			synchronized (GuiceContext.class) {
				if (me == null)
					me = new GuiceContext();
			}
		return me;
	}
	
	/**
	 * 从当前上下文中获取对象
	 * @param <T> 对象类型
	 * @param clazz 要获取对象的 class
	 * @return 对象实例
	 */
	public <T> T getBean(Class<T> clazz) {
		Assert.isTrue(initialized, "对象未被初始化");
		return injector.getInstance(clazz);
	}
	
	/**
	 * 使用当前上下文为对象注入依赖的成员对象
	 * @param o 要注入成员的对象
	 */
	public void injectorMembers(Object o) {
		Assert.isTrue(initialized, "对象未被初始化");
		injector.injectMembers(o);
	}
	
	/**
	 * 添加 模块提供者
	 * @param providers 模块提供者实例
	 * @return 全局上下文对象自身
	 */
	public GuiceContext addModuleProvider(ModuleProvider... providers) {
		Assert.isFalse(initialized, "对象已经完成初始化，该方法不再生效");
		for (ModuleProvider provider : providers) {
			this.providers.add(provider);
		}
		return this;
	}
	
	/**
	 * 初始化方法,该对象只会被初始化一次
	 * @return 全局上下文对象自身
	 */
	public GuiceContext init() {
		if (initialized)
			return this;
		synchronized (GuiceContext.class) {
			if (initialized)
				return this;
			final ArrayList<Module> modules = new ArrayList<Module>();
			for (ModuleProvider provider : providers) {
				modules.addAll(provider.getModules());
			}
			injector = Guice.createInjector(new Iterable<Module>() {
				@Override
				public Iterator<Module> iterator() {
					return modules.iterator();
				}
			});
			initialized = true;
			return this;
		}
	}
}
