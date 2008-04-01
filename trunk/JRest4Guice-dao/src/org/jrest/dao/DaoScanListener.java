package org.jrest.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jrest.core.util.ClassScanListener;
import org.jrest.dao.annotations.Dao;

import com.google.inject.Binder;
import com.google.inject.Module;

@SuppressWarnings("unchecked")
public class DaoScanListener implements ClassScanListener {
	private Set<String> binds;
	
	private List<Class<?>> daos;
	
	private DaoPersistProviderType persitProviderType;
	
	public DaoScanListener(){
		String persitProviderType = System.getProperty("persitProviderType");
		if(persitProviderType == null || persitProviderType.trim().equals(""))
			throw new RuntimeException("");
		
		if(persitProviderType.equalsIgnoreCase("JPA")){
			this.persitProviderType = DaoPersistProviderType.JPA;
		}else if(persitProviderType.equalsIgnoreCase("HIBERNATE")){
			this.persitProviderType = DaoPersistProviderType.HIBERNATE;
		}else if(persitProviderType.equalsIgnoreCase("DB4O")){
			this.persitProviderType = DaoPersistProviderType.DB4O;
		}else if(persitProviderType.equalsIgnoreCase("JDBC")){
			this.persitProviderType = DaoPersistProviderType.JDBC;
		}else
			this.persitProviderType = DaoPersistProviderType.JPA;
	}
	
	public DaoScanListener(DaoPersistProviderType persitProviderType){
		this.persitProviderType = persitProviderType;
	}
	
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
		this.daos = new ArrayList<Class<?>>(0);
		this.binds = new HashSet<String>();
	}
}
