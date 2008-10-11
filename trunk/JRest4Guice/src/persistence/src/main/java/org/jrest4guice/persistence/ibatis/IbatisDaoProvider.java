package org.jrest4guice.persistence.ibatis;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * DAO接口实例化的提供者
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * @param <T> DAO接口
 */
public class IbatisDaoProvider<T> implements Provider<T> {

	@Inject
	private IbatisDaoDynamicProxy proxy;

	private Class<T> clazz;

	public IbatisDaoProvider(Class<T> clazz) {
		this.clazz = clazz;
	}

	public T get() {
		return (T) proxy.createDao(this.clazz);
	}

	public static <T> Provider<T> create(Class<T> providerType) {
		return new IbatisDaoProvider<T>(providerType);
	}
}
