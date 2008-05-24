package org.jrest4guice;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
public class ServiceRoute {
	private String id;

	private ServiceRoute parent;

	private Class<?> serviceClass;

	private String paramName;

	private Map<String, ServiceRoute> children = new HashMap<String, ServiceRoute>();
	
	private Map<String, Method> restMethod = new HashMap<String, Method>();

	public ServiceRoute() {
		this("");
	}

	public ServiceRoute(String paramName) {
		this.paramName = paramName;
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public void addChild(String key, ServiceRoute child) {
		if (children.containsKey(key))
			return;
		children.put(key, child);
		child.setParent(this);
	}

	public ServiceRoute getChild(String key) {
		return children.get(key);
	}

	public String getParamName() {
		return paramName;
	}

	public ServiceRoute getParent() {
		return parent;
	}

	public Class<?> getServiceClass() {
		return serviceClass;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public void setParent(ServiceRoute parent) {
		this.parent = parent;
	}

	public void setServiceClass(Class<?> res) {
		this.serviceClass = res;
	}

}
