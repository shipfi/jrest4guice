package org.jrest4guice.rest;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jrest4guice.client.JRestClient;
import org.jrest4guice.client.ModelMap;
import org.jrest4guice.commons.lang.ClassUtils;
import org.jrest4guice.rest.annotations.Remote;

import com.google.inject.Singleton;
import com.google.inject.cglib.proxy.Enhancer;
import com.google.inject.cglib.proxy.MethodInterceptor;
import com.google.inject.cglib.proxy.MethodProxy;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ：86895156)</a>
 * 
 */
@Singleton
@SuppressWarnings("unchecked")
public class RemoteServiceDynamicProxy implements MethodInterceptor {
	private Enhancer enhancer = new Enhancer();

	private List<Method> methods;

	private List<String> remoteServiceUrls;

	private Map<String, String> service_url_relation;

	public RemoteServiceDynamicProxy() {
		Properties property = new Properties();
		try {
			property.load(this.getClass().getClassLoader().getResourceAsStream(
					"remote.properties"));
		} catch (Exception e) {
			throw new RuntimeException("在Classpath中无法获取remote.properties文件");
		}

		Object service_url = property.get("service_url");
		if (service_url == null || service_url.toString().trim().equals(""))
			throw new RuntimeException(
					"在remote.properties文件中没有设置service_url参数值");

		String[] urls = service_url.toString().split(",");
		this.remoteServiceUrls = Arrays.asList(urls);

		this.service_url_relation = new HashMap<String, String>();
	}

	public <T> T createRemoteService(Class<T> serviceClazz) {
		if (!serviceClazz.isAnnotationPresent(Remote.class))
			throw new RuntimeException(serviceClazz.getName() + "不支持远程调用！");

		this.methods = ClassUtils.getSortedMethodList(serviceClazz);

		enhancer.setSuperclass(serviceClazz);
		enhancer.setCallback(this);
		return (T) enhancer.create();
	}

	@Override
	public Object intercept(Object instance, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		Object result = null;

		JRestClient client = new JRestClient();

		Class<?> clazz = method.getDeclaringClass();
		Remote remote = clazz.getAnnotation(Remote.class);
		String name = remote.value();
		if (name == null || name.trim().equals(""))
			name = clazz.getName();

		int index = -1;
		for (Method m : methods) {
			index++;
			if (m.equals(method)) {
				break;
			}
		}

		ModelMap<String, Object> parameters = new ModelMap<String, Object>();
		parameters.put(ModelMap.RPC_ARGS_KEY, args);

		String serviceUrl = "";
		if (this.service_url_relation.containsKey(clazz.getName())) {
			serviceUrl = this.service_url_relation.get(clazz.getName());
			serviceUrl += Remote.REMOTE_SERVICE_PREFIX + "?"
			+ Remote.REMOTE_SERVICE_NAME_KEY + "=" + name + "&"
			+ Remote.REMOTE_SERVICE_METHOD_INDEX_KEY + "=" + index;
			try {
				result = client.callRemote("http://" + serviceUrl,
						RequestProcessor.METHOD_OF_POST, parameters);
			} catch (Exception e) {
				result = this.try2lookupRemoteServiceAndExcute(result, client,
						clazz, name, index, parameters);
			}
		} else {
			result = this.try2lookupRemoteServiceAndExcute(result, client,
					clazz, name, index, parameters);
		}
		return result;
	}

	private Object try2lookupRemoteServiceAndExcute(Object result,
			JRestClient client, Class<?> clazz, String name, int index,
			ModelMap<String, Object> parameters) {
		String serviceUrl;
		for (String surl : this.remoteServiceUrls) {
			if (surl.trim().equals(""))
				continue;
			if (!surl.endsWith("/"))
				surl = surl + "/";

			serviceUrl = surl + Remote.REMOTE_SERVICE_PREFIX + "?"
					+ Remote.REMOTE_SERVICE_NAME_KEY + "=" + name + "&"
					+ Remote.REMOTE_SERVICE_METHOD_INDEX_KEY + "=" + index;
			try {
				result = client.callRemote("http://" + serviceUrl,
						RequestProcessor.METHOD_OF_POST, parameters);
				this.service_url_relation.put(clazz.getName(), surl);
				break;
			} catch (Exception e) {
				this.service_url_relation.remove(clazz.getName());
			}
		}
		return result;
	}
}
