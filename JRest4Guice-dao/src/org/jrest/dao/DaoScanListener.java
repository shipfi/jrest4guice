package org.jrest.dao;

import java.util.ArrayList;
import java.util.List;

import org.jrest.core.util.ClassScanListener;
import org.jrest.dao.annotations.Dao;

import com.google.inject.Binder;
import com.google.inject.Module;

@SuppressWarnings("unchecked")
public class DaoScanListener implements ClassScanListener {
	final List<Class<?>> daos;
	
	private DaoPersistProviderType persitProviderType;
	
	public DaoScanListener(DaoPersistProviderType persitProviderType){
		daos = new ArrayList<Class<?>>(0);
		this.persitProviderType = persitProviderType;
	}
	
	@Override
	public void onComplete(List<Module> modules) {
		Module module = new Module(){
			@Override
			public void configure(Binder binder) {
				for(Class clazz :daos){
					binder.bind(clazz).toProvider(DaoProvider.create(clazz,persitProviderType));
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
	}
}
