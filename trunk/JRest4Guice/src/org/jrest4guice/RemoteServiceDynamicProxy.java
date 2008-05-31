package org.jrest4guice;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jrest4guice.annotations.Remote;
import org.jrest4guice.client.JRestClient;
import org.jrest4guice.core.util.ParameterNameDiscoverer;

import com.google.inject.Singleton;

@Singleton
@SuppressWarnings("unchecked")
public class RemoteServiceDynamicProxy implements InvocationHandler {
	private final static Log log = LogFactory
			.getLog(RemoteServiceDynamicProxy.class);

	public <T> T createRemoteService(Class<T> serviceClazz) {
		if (!serviceClazz.isAnnotationPresent(Remote.class))
			throw new RuntimeException(serviceClazz.getName() + "不支持远程调用！");

		return (T) Proxy.newProxyInstance(serviceClazz.getClassLoader(),
				new Class[] { serviceClazz }, this);
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = null;

		JRestClient client = new JRestClient();

		Class<?> clazz = proxy.getClass();
		Remote remote = clazz.getAnnotation(Remote.class);
		String name = remote.value();
		if (name == null || name.trim().equals(""))
			name = clazz.getName();

		int index = -1;
		Method[] methods = clazz.getMethods();
		for (Method m : methods) {
			index++;
			if (m.equals(method)) {
				break;
			}
		}

		Map<String, String> urlParam = null;
		String serviceUrl = "http://localhost/resource/"
				+ Remote.REMOTE_SERVICE_PREFIX + "?"
				+ Remote.REMOTE_SERVICE_NAME_KEY + "=" + name + "&"
				+ Remote.REMOTE_SERVICE_METHOD_INDEX_KEY + "=" + index;
		if (args != null && args.length > 0) {
			ParameterNameDiscoverer pnDiscoverer = new ParameterNameDiscoverer();
			String[] parameterNames = pnDiscoverer.getParameterNames(method);
			urlParam = new HashMap<String, String>();
			int i = 0;
			for (Object arg : args) {
				urlParam.put(parameterNames[i], arg.toString());
				i++;
			}
		}

		result = client.callRemote(serviceUrl, RequestProcessor.METHOD_OF_POST,
				urlParam);

		return result;
	}
}
