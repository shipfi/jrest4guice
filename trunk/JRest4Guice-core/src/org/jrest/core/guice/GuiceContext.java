package org.jrest.core.guice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jrest.core.persist.PersistProviderType;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * 全局上下文对象实体
 */
public class GuiceContext {

	private static volatile GuiceContext me;

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

	private Injector injector;

	private PersistProviderType persitProviderType;
	
	private Set<GuiceModuleProvider> moduleProviders;
	private List<Module> guiceModules;

	private GuiceContext() {
		moduleProviders = new HashSet<GuiceModuleProvider>();
		guiceModules = new ArrayList<Module>(0);
	}

	/**
	 * 从Guice上下文中获取对象
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public <T> T getBean(Class<T> clazz) {
		return injector.getInstance(clazz);
	}

	public final PersistProviderType getPersitProviderType() {
		return persitProviderType;
	}
	
	public void setPersitProviderType(PersistProviderType persitProviderType) {
		this.persitProviderType = persitProviderType;
	}

	public GuiceContext addModuleProvider(GuiceModuleProvider provider){
		this.moduleProviders.add(provider);
		return this;
	}

	public GuiceContext addGuiceModules(List<Module> modules){
		this.guiceModules.addAll(modules);
		return this;
	}
	
	public void init() {
		for(GuiceModuleProvider provider :this.moduleProviders)
			guiceModules.addAll(provider.getModules());
			
		// 初始化Guice的注入器
		injector = Guice.createInjector(new Iterable<Module>() {
			@Override
			public Iterator<Module> iterator() {
				return guiceModules.iterator();
			}
		});
	}
}
