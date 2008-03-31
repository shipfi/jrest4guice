package org.jrest.dao;

import org.jrest.dao.jpa.JpaRegister;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class DaoProvider<T> implements Provider<T> {
	@Inject
	private DynamicProxy proxy;
	@Inject
	private JpaRegister register;

	private Class<T> clazz;

	public DaoProvider(Class<T> clazz) {
		this.clazz = clazz;
	}

	public T get() {
//		DynamicProxy proxy = new DynamicProxy();
//		JpaRegister register = GuiceContext.getInstance().getInstance(JpaRegister.class);;
		proxy.setRegister(register);
		return (T) proxy.createDao(this.clazz);
	}

	public static <T> Provider<T> create(Class<T> type) {
		return new DaoProvider<T>(type);
	}
}
