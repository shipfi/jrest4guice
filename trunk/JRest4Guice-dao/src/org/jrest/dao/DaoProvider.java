package org.jrest.dao;

import org.jrest.dao.jpa.JpaRegister;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class DaoProvider<T> implements Provider<T> {
	@Inject
	private DynamicProxy proxy;
	private Register register;

	private Class<T> clazz;

	private DaoPersistProviderType persitProviderType;	

	public DaoProvider(Class<T> clazz,DaoPersistProviderType persitProviderType) {
		this.clazz = clazz;
		this.persitProviderType = persitProviderType;
	}

	public T get() {
		if(this.register == null){
			switch (this.persitProviderType) {
			case JPA:
				this.register = new JpaRegister();
				break;
			default:
				break;
			}
		}else
			proxy.setRegister(register);
		
		return (T) proxy.createDao(this.clazz);
	}

	public static <T> Provider<T> create(Class<T> providerType,DaoPersistProviderType persitProviderType) {
		return new DaoProvider<T>(providerType,persitProviderType);
	}
}
