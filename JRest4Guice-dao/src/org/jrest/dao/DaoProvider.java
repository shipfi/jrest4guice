package org.jrest.dao;

import org.jrest.core.guice.GuiceContext;
import org.jrest.dao.jpa.JpaRegister;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class DaoProvider<T> implements Provider<T> {
	@Inject
	private DynamicProxy proxy;
	private Register register;

	private Class<T> clazz;

	public DaoProvider(Class<T> clazz) {
		this.clazz = clazz;
	}

	public T get() {
		if(this.register == null){
			switch (GuiceContext.getInstance().getPersitProviderType()) {
			case JPA:
				this.register = new JpaRegister();
				break;
			default:
				break;
			}
		}
		
		if(this.register == null)
			throw new RuntimeException("没有指定持久上下文的实现类型（如：JPA/HIBERNATE/DB4O/JDBC）");

		proxy.setRegister(register);
		
		return (T) proxy.createDao(this.clazz);
	}

	public static <T> Provider<T> create(Class<T> providerType) {
		return new DaoProvider<T>(providerType);
	}
}
