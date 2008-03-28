package org.jrest.test;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.google.inject.Binder;
import com.google.inject.Module;

@SuppressWarnings("unchecked")
public class ContactGuiceModule implements Module {
	@Override
	public void configure(Binder binder) {
		try {
			// Bind Context to the default InitialContext.
			binder.bind(Context.class).toInstance(this.getInitialContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取本机的远程JNDI上下文
	 * @return
	 * @throws Exception
	 */
	InitialContext getInitialContext() throws Exception {
		return getRemoteInitialContext("localhost", 1099);
	}
	
	/**
	 * 获取远程的JNDI上下文
	 * @modelMap host	服务器名称或IP地址
	 * @modelMap port	JNDI服务的端口
	 * @return
	 * @throws Exception
	 */
	InitialContext getRemoteInitialContext(String host, int port)
			throws Exception {
		Hashtable env = new Hashtable();
		env.put("java.naming.factory.initial",
				"org.jnp.interfaces.NamingContextFactory");
		env.put("java.naming.factory.url.pkgs",
				"org.jboss.naming:org.jnp.interfaces");
		env.put("java.naming.provider.url", host + ":" + port);
		return new InitialContext(env);
	}
}
