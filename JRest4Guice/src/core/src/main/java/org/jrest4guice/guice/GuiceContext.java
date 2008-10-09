package org.jrest4guice.guice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jrest4guice.cache.CacheGuiceModuleProvider;
import org.jrest4guice.commons.lang.Assert;
import org.jrest4guice.interceptor.InterceptorGuiceModuleProvider;
import org.jrest4guice.security.SecurityGuiceModuleProvider;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * 全局上下文对象实体
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 */
public class GuiceContext {

	private static volatile GuiceContext me;

	private volatile boolean initialized = false;
	
	private boolean useSecurity;

	/**
	 * 模块提供者集合
	 */
	private final Set<ModuleProvider> providers; 
	
	/**
	 * 用户自定义的模块集合
	 */
	private final Set<Module> userModules;

	private Injector injector;

	protected GuiceContext() {
		providers = new HashSet<ModuleProvider>();
		userModules = new HashSet<Module>();
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
		for (ModuleProvider provider : providers) {
			this.providers.add(provider);
		}
		return this;
	}

	public GuiceContext addUserModule(Module... modules) {
		for (Module module : modules)
			this.userModules.add(module);
		return this;
	}
	
	public boolean isInitialized() {
		return initialized;
	}


	/**
	 * 打开自定义的拦截器支持，允许通过@Interceptors来支持自定义的拦截器
	 * @param packages
	 * @return
	 */
	public GuiceContext enableCustomInterceptor(String... packages){
		this.addModuleProvider(new InterceptorGuiceModuleProvider(packages));
		return this;
	}

	/**
	 * 打开JAAS支持
	 * @return
	 */
	public GuiceContext useSecurity(){
		this.useSecurity = true;
		this.addModuleProvider(new SecurityGuiceModuleProvider());
		return this;
	}
	
	/**
	 * 打开SNA支持
	 * @param packages cache提供者的扫描路径
	 * @return
	 */
	public GuiceContext useCache(String... packages){
		this.addModuleProvider(new CacheGuiceModuleProvider());
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
			
			for (Module module : this.userModules)
				this.userModules.add(module);
			
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

	public boolean isUseSecurity() {
		return useSecurity;
	}
}
