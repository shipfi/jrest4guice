package org.jrest4guice.context;

import java.lang.reflect.Method;

public class RestMethod {
	private String path;
	private Method method;
	
	public RestMethod(String path, Method method) {
		super();
		this.path = path;
		this.method = method;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
}
