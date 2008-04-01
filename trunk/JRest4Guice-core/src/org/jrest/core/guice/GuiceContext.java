package org.jrest.core.guice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jrest.core.util.ClassPathScanner;
import org.jrest.core.util.ClassScanListener;
import org.jrest.core.util.ClassPathScanner.ClassFilter;

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
	 * @param scanPaths 系统自动扫描的包名
	 * @param listeners	系统自动扫描的监听器，用来加载用户的附加Guice module
	 */
	public void init(List<String> scanPaths,final List<ClassScanListener> listeners) {
		this.init(null,scanPaths, listeners);
	}

	/**
	 * 初始化Guice中的模块
	 * @param modules	用户指定的Guice模块
	 * @param scanPaths 系统自动扫描的包名
	 * @param listeners	系统自动扫描的监听器，用来加载用户的附加Guice module
	 */
	public void init(List<Module> modules, List<String> scanPaths,final List<ClassScanListener> listeners) {
		//如果已经初始化，则直接返回
		if (this.injector != null)
			return;
		
		Set<String> scanPathSet = new HashSet<String>();
		
		if(modules == null)
			modules = new ArrayList<Module>(0);

		List<Class<?>> list = new ArrayList<Class<?>>();

		if (scanPaths != null) {
			scanPathSet.addAll(scanPaths);
		}

		//添加jpa4guice到扫描库
		scanPathSet.add("org.jrest.dao");
		try{
			for(ClassScanListener listener :listeners)
				listener.onStart();
			for (String scanPath : scanPathSet) {
				list.addAll(new ClassPathScanner(scanPath, new ClassFilter() {
					public boolean accept(Class<?> clazz) {
						for(ClassScanListener listener :listeners)
							listener.onScan(clazz);

						return GuiceModuleProvider.class.isAssignableFrom(clazz);
					}
				}).scan());
			}
			for(ClassScanListener listener :listeners)
				listener.onComplete(modules);
		}catch(Exception e){
		}

		final List<Module> _modules = new ArrayList<Module>(0);
		_modules.addAll(modules);

		try {
			for (Class<?> clazz : list) {
				_modules.addAll(((GuiceModuleProvider) clazz.newInstance())
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

	public <T> T getBean(Class<T> key) {
		return this.injector.getInstance(key);
	}
}
