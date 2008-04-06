package org.jrest.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * DAO接口实例化的提供者
 * @author <a href="mailto:gzyangfan@gmail.com">gzYangfan</a>
 * @param <T> DAO接口
 */
public class DaoProvider<T> implements Provider<T> {

	@Inject
	private DynamicProxy proxy;

	private Class<T> clazz;

	public DaoProvider(Class<T> clazz) {
		this.clazz = clazz;
	}

	@SuppressWarnings("unchecked")
	public T get() {
		return (T) proxy.createDao(this.clazz);
	}

	public static <T> Provider<T> create(Class<T> providerType) {
		return new DaoProvider<T>(providerType);
	}
}
