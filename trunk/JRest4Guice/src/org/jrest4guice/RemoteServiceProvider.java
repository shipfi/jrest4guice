package org.jrest4guice;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * DAO接口实例化的提供者
 * @author <a href="mailto:gzyangfan@gmail.com">gzYangfan</a>
 * @param <T> DAO接口
 */
public class RemoteServiceProvider<T> implements Provider<T> {

	@Inject
	private RemoteServiceDynamicProxy proxy;

	private Class<T> clazz;

	public RemoteServiceProvider(Class<T> clazz) {
		this.clazz = clazz;
	}

	@SuppressWarnings("unchecked")
	public T get() {
		return (T) proxy.createRemoteService(this.clazz);
	}

	public static <T> Provider<T> create(Class<T> providerType) {
		return new RemoteServiceProvider<T>(providerType);
	}
}
