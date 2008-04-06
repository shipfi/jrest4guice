package org.jrest.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jrest.core.util.ClassScanListener;
import org.jrest.dao.annotations.Dao;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

/**
 * DAO类扫描监听器
 */
@SuppressWarnings("unchecked")
public class DaoScanListener implements ClassScanListener {

	class DaoModule extends AbstractModule {

		private Set<String> binds = new HashSet<String>();

		@Override
		protected void configure() {
			for (Class clazz : daos) {
				String name = clazz.getName();
				if (binds.contains(name))
					continue;
				binds.add(name);
				bind(clazz).toProvider(DaoProvider.create(clazz));
			}
		}

	}

	private List<Class<?>> daos = new ArrayList<Class<?>>();

	public DaoScanListener(List<Class<?>> classes) {
		for (Class<?> clz : classes)
			onScan(clz);
	}

	@Override
	public Module onComplete() {
		Module module = new DaoModule();
		return module;
	}

	@Override
	public void onScan(Class<?> clazz) {
		if (clazz.isAnnotationPresent(Dao.class))
			daos.add(clazz);
	}

}