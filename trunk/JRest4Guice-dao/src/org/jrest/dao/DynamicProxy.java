package org.jrest.dao;

import java.lang.reflect.Proxy;

public class DynamicProxy {
	
	private Register register;
	
	public void setRegister(Register register) {
		this.register = register;
	}
	

	public Object createDao(Class<?> daoClazz) {
		DynamicInvoker invoker = new DynamicInvoker();
		invoker.setRegister(register);
		return Proxy.newProxyInstance(daoClazz.getClassLoader(), new Class[] { daoClazz }, invoker);
	}

}
