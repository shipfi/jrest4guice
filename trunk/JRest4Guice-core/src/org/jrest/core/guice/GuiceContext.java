package org.jrest.core.guice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jrest.core.persist.PersistProviderType;
import org.jrest.core.util.ClassPathScanner;
import org.jrest.core.util.ClassScanListener;
import org.jrest.core.util.ClassPathScanner.ClassFilter;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * 全局上下文对象实体
 */
public class GuiceContext {

	private static class SingletonHolder {
		static GuiceContext instance = new GuiceContext();
	}

	public static GuiceContext getInstance() {
		return SingletonHolder.instance;
	}

	private Injector injector;

	private final Set<String> scanPathSet;

	private PersistProviderType persitProviderType;

	private GuiceContext() {
		scanPathSet = new HashSet<String>();
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

	/**
	 * 初始化Guice中的模块
	 * @param modules 用户指定的Guice模块
	 * @param scanPaths 系统自动扫描的包名
	 * @param listeners 系统自动扫描的监听器，用来加载用户的附加Guice module
	 */
	public void init(List<Module> modules, List<String> scanPaths, final List<ClassScanListener> listeners) {
		// 如果已经初始化，则直接返回
		if (injector != null)
			return;

		if (modules == null)
			modules = new ArrayList<Module>(0);

		List<Class<?>> list = new ArrayList<Class<?>>();

		if (scanPaths != null) {
			scanPathSet.addAll(scanPaths);
		}

		try {
			for (String scanPath : scanPathSet) {
				list.addAll(new ClassPathScanner(scanPath, new ClassFilter() {
					public boolean accept(Class<?> clazz) {
						for (ClassScanListener listener : listeners)
							listener.onScan(clazz);
						return !clazz.isInterface() && GuiceModuleProvider.class.isAssignableFrom(clazz);
					}
				}).scan());
			}
			for (ClassScanListener listener : listeners)
				modules.add(listener.onComplete());
		} catch (Exception e) {
		}

		final List<Module> _modules = new ArrayList<Module>(0);
		_modules.addAll(modules);

		try {
			for (Class<?> clazz : list) {
				_modules.addAll(((GuiceModuleProvider) clazz.newInstance()).getModules());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 初始化Guice的注入器
		injector = Guice.createInjector(new Iterable<Module>() {
			@Override
			public Iterator<Module> iterator() {
				return _modules.iterator();
			}
		});
	}

	/**
	 * 初始化Guice中的模块
	 * @param scanPaths 系统自动扫描的包名
	 * @param listeners 系统自动扫描的监听器，用来加载用户的附加Guice module
	 */
	public void init(List<String> scanPaths, final List<ClassScanListener> listeners) {
		this.init(null, scanPaths, listeners);
	}

	public GuiceContext useDAO() {
		addTransactionSupport();
		scanPathSet.add("org.jrest.dao");
		return this;
	}

	public GuiceContext useJPA() {
		addTransactionSupport();
		persitProviderType = PersistProviderType.JPA;
		return this;
	}

	private void addTransactionSupport() {
		scanPathSet.add("org.jrest.core");
	}
}
