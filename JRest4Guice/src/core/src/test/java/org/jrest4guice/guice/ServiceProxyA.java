package org.jrest4guice.guice;

import com.google.inject.Inject;

public class ServiceProxyA {
	@Inject
	private ServiceA serviceA;

	@Inject
	private ServiceB serviceB;
	
	public ServiceProxyA(){
	}
	
	public ServiceProxyA(ServiceA a,ServiceB b){
		this.serviceA = a;
		this.serviceB = b;
	}
	
	public void say(){
		this.serviceA.sayA();
		this.serviceB.sayB();
	}
}
