package org.cnoss.guice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cnoss.guice.ClassPathScanner.ClassFilter;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class GuiceContext {
	private Injector injector;

	private GuiceContext() {
	}

	private static class SingletonHolder {
		static GuiceContext instance = new GuiceContext();
	}

	public static GuiceContext getInstance() {
		return SingletonHolder.instance;
	}

	public void init(List<Module> modules,String scanPath) {
		List<Class<?>> list = new ClassPathScanner(scanPath, new ClassFilter() {
			public boolean accept(Class<?> clazz) {
				return GuiceSupportAble.class.isAssignableFrom(clazz);
			}
		}).scan();

		final List<Module> _modules = new ArrayList<Module>(0);
		if(modules != null)
			_modules.addAll(modules);
		
		try {
			for (Class<?> clazz : list) {
				_modules.addAll(((GuiceSupportAble) clazz.newInstance())
						.getModules());
			}
		} catch (Exception e) {
		}
		
		// 初始化Guice的注入器
		injector = Guice.createInjector(new Iterable<Module>() {
			@Override
			public Iterator<Module> iterator() {
				return _modules.iterator();
			}
		});
	}

	public <T> T getInstance(Class<T> key) {
		return this.injector.getInstance(key);
	}
}
