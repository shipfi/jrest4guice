package org.jrest.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jrest.core.guice.GuiceContext;
import org.jrest.core.util.ClassScanListener;
import org.jrest.dao.annotations.Dao;

import com.google.inject.Binder;
import com.google.inject.Module;

@SuppressWarnings("unchecked")
public class DaoScanListener implements ClassScanListener {
	private Set<String> binds;
	
	private List<Class<?>> daos;
	
	@Override
	public void onComplete(List<Module> modules) {
		Module module = new Module(){
			@Override
			public void configure(Binder binder) {
				for(Class clazz :daos){
					String name = clazz.getName();
					if(binds.contains(name))
						continue;
					
					binds.add(name);
					binder.bind(clazz).toProvider(DaoProvider.create(clazz,GuiceContext.getInstance().getPersitProviderType()));
				}
			}
		};
		
		modules.add(module);
	}

	@Override
	public void onScan(Class<?> clazz) {
		if(clazz.isAnnotationPresent(Dao.class))
			daos.add(clazz);
	}

	@Override
	public void onStart() {
		this.daos = new ArrayList<Class<?>>(0);
		this.binds = new HashSet<String>();
	}
}
