package org.cnoss.guice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cnoss.util.ClassPathScanner;
import org.cnoss.util.ClassPathScanner.ClassFilter;

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

	/**
	 * 初始化Guice中的模块
	 * @param modules	用户指定的Guice模块
	 * @param scanPaths 系统自动扫描的包名
	 */
	public void init(List<Module> modules, List<String> scanPaths) {
		//如果已经初始化，则直接返回
		if (this.injector != null)
			return;

		List<Class<?>> list = new ArrayList<Class<?>>();

		if (scanPaths == null) {
			scanPaths = new ArrayList<String>();
		}
		//添加jpa4guice到扫描库
		scanPaths.add("org.jpa4guice");

		for (String scanPath : scanPaths) {
			list.addAll(new ClassPathScanner(scanPath, new ClassFilter() {
				public boolean accept(Class<?> clazz) {
					return GuiceSupportAble.class.isAssignableFrom(clazz);
				}
			}).scan());
		}

		final List<Module> _modules = new ArrayList<Module>(0);
		if (modules != null)
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
