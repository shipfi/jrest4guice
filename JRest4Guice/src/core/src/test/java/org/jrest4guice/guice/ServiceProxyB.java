package org.jrest4guice.guice;

import com.google.inject.Inject;

public class ServiceProxyB {
	private ServiceA serviceA;
	private ServiceB serviceB;
	
	@Inject
	public ServiceProxyB(ServiceA a,ServiceB b){
		this.serviceA = a;
		this.serviceB = b;
	}
	
	public void say(){
		this.serviceA.sayA();
		this.serviceB.sayB();
	}
}
