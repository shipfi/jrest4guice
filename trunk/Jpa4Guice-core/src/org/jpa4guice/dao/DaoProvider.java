package org.jpa4guice.dao;

import org.cnoss.guice.GuiceContext;

import com.google.inject.Provider;

public class DaoProvider<T> implements Provider<T> {
	private Class<T> clazz;

	public DaoProvider(Class<T> clazz) {
		this.clazz = clazz;
	}

	public T get() {
		DaoCglibProxy proxy = GuiceContext.getInstance().getInstance(
				DaoCglibProxy.class);
		return proxy.getProxy(this.clazz);
	}

	public static <T> Provider<T> create(Class<T> type) {
		return new DaoProvider<T>(type);
	}
}
