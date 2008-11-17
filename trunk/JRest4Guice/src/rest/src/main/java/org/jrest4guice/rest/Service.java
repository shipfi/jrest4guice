package org.jrest4guice.rest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.jrest4guice.rest.annotations.HttpMethodType;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
public class Service {
	private Object instance;
	private Map<HttpMethodType, Method> methods = new HashMap<HttpMethodType, Method>(0);
	
	public Service(Object instance, Map<HttpMethodType, Method> methods) {
		super();
		this.instance = instance;
		this.methods = methods;
	}
	public Object getInstance() {
		return instance;
	}
	public void setInstance(Object instance) {
		this.instance = instance;
	}
	public Map<HttpMethodType, Method> getMethod() {
		return methods;
	}
	public void setMethod(Map<HttpMethodType, Method> methods) {
		this.methods = methods;
	}
}
