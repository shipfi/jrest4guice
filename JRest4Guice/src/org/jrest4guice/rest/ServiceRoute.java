package org.jrest4guice.rest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ：86895156)</a>
 *
 */
public class ServiceRoute {
	private String id;

	private ServiceRoute parent;

	private Class<?> serviceClass;

	private String paramName;

	private Map<String, ServiceRoute> routeChildren = new HashMap<String, ServiceRoute>();
	private Map<String, List<ServiceRoute>> paramChildren = new HashMap<String, List<ServiceRoute>>();
	
	private Method method;
	
	public ServiceRoute() {
		this("");
	}

	public ServiceRoute(String paramName) {
		this.paramName = paramName;
		this.id = UUID.randomUUID().toString();
	}

	public Method getMethod() {
		return method;
	}

	public void addRestMethod(Method method) {
		this.method = method;
	}

	public String getId() {
		return id;
	}

	public void addRouteChild(String key, ServiceRoute child) {
		if (routeChildren.containsKey(key))
			return;
		routeChildren.put(key, child);
		child.setParent(this);
	}

	public ServiceRoute getRouteChild(String key) {
		return routeChildren.get(key);
	}

	public void addParamChild(String key, ServiceRoute child) {
		List<ServiceRoute> pRoutes = new ArrayList<ServiceRoute>();
		if(paramChildren.containsKey(key))
			pRoutes = paramChildren.get(key);
		else
			paramChildren.put(key, pRoutes);
		child.setParent(this);
		
		pRoutes.add(child);
	}

	public List<ServiceRoute> getParamChild(String key) {
		return paramChildren.get(key);
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

	public Map<String, ServiceRoute> getRouteChildren() {
		return routeChildren;
	}

	public Map<String, List<ServiceRoute>> getParamChildren() {
		return paramChildren;
	}
}
