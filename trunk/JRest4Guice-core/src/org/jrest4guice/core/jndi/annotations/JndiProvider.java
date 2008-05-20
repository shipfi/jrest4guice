package org.jrest4guice.core.jndi.annotations;

import javax.naming.Context;
import javax.naming.NamingException;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
public class JndiProvider<T> implements Provider<T> {
	@Inject
	Context context;

	final String name;
	final Class<T> type;

	JndiProvider(Class<T> type, String name) {
		this.name = name;
		this.type = type;
	}

	public T get() {
		try {
			return type.cast(context.lookup(name));
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates a JNDI provider for the given type and name.
	 */
	public static <T> Provider<T> fromJndi(Class<T> type, String name) {
		return new JndiProvider<T>(type, name);
	}
}
