package org.jrest.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jrest.core.guice.ModuleProvider;
import org.jrest.core.util.Assert;
import org.jrest.core.util.ClassUtils;
import org.jrest.dao.actions.Action;
import org.jrest.dao.actions.ActionClassFilter;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Dao上下文对象<br>
 * 该对象负责管理Dao层的依赖关系，并负责创建Dao接口的实体。
 * @author <a href="mailto:gzyangfan@gmail.com">gzYangfan</a>
 */
public class DaoContext {
	
	private final static Log log = LogFactory.getLog(DaoContext.class);
	
	private static volatile DaoContext me;
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static DaoContext getInstance() {
		if (me == null)
			synchronized (DaoContext.class) {
				if (me == null)
					me = new DaoContext();
			}
		return me;
	}
	
	private final Set<String> scanPaths;
	private final Set<Class<?>> exclude;
	private final Set<Class<?>> include;
	
	private boolean initialized = false;
	private Injector injector;
	private Register register;
	
	private DaoContext() {
		scanPaths = new HashSet<String>();
		exclude = new HashSet<Class<?>>();
		include = new HashSet<Class<?>>();
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
	 * 初始化方法,该类只会被初始化一次
	 */
	@SuppressWarnings("unchecked")
	public synchronized void init() {
		if (injector != null)
			return;
		
		final List<Class<?>> classes = getAllClasses();
		
		injector = Guice.createInjector(new Iterable<Module>() {
			@Override
			public Iterator<Module> iterator() {
				List<Module> modules = new ArrayList<Module>();
				
				ModuleProviderFilter moduleFilter = new ModuleProviderFilter(classes);
				for (Class<ModuleProvider> clz : moduleFilter.getProviderClasses()) {
					try {
						modules.add(clz.newInstance().getModule());
					} catch (Exception e) {
						// 不应该出现的错误
						log.error("无法实例化 ModuleProvider 类:" + clz.getName(), e);
					}
				}
				
				DaoScanListener daoScaner = new DaoScanListener(classes);
				modules.add(daoScaner.onComplete());
				
				return modules.iterator();
			}
		});
		
		register = injector.getInstance(Register.class);
		ActionClassFilter actionFilter = new ActionClassFilter(classes);
		for (Class<Action> clz : actionFilter.getActionClasses()) {
			register.register(clz);
		}
		
		initialized = true;
	}
	
	private List<Class<?>> getAllClasses() {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		for (String path : scanPaths) {
			classes.addAll(ClassUtils.getClasses(path));
		}
		classes.removeAll(exclude);
		classes.addAll(include);
		return classes;
	}
	
	/**
	 * 添加排除类型
	 * @param classes
	 */
	public void addExcludeClasses(Class<?>... classes) {
		Assert.isTrue(!initialized, "对象已经初始化的,该方法不再生效");
		for (Class<?> clz : classes) {
			if (exclude.contains(clz))
				continue;
			exclude.add(clz);
		}
	}
	
	/**
	 * 添加包括类型
	 * @param classes
	 */
	public void addIncludeClasses(Class<?>... classes) {
		Assert.isTrue(!initialized, "对象已经初始化的,该方法不再生效");
		for (Class<?> clz : classes) {
			if (include.contains(clz))
				continue;
			include.add(clz);
		}
	}
	
	/**
	 * 添加扫描路径
	 * @param paths
	 */
	public void addScanPaths(String... paths) {
		Assert.isTrue(!initialized, "对象已经初始化的,该方法不再生效");
		for (String s : paths) {
			if (scanPaths.contains(s))
				continue;
			scanPaths.add(s);
		}
	}
	
}
