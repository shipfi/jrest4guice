package org.jrest4guice.guice;

import org.jrest4guice.guice.GuiceContext;

public class ServiceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GuiceContext.getInstance().init();
		
		long start = System.currentTimeMillis();
		for(int i=0;i<5000000;i++){
			ServiceProxyB bean = GuiceContext.getInstance().getBean(ServiceProxyB.class);
			bean.say();
		}
		long end = System.currentTimeMillis();
		
		System.out.println("构造注入："+(end-start));

		start = System.currentTimeMillis();
		for(int i=0;i<5000000;i++){
			ServiceProxyA bean = GuiceContext.getInstance().getBean(ServiceProxyA.class);
			bean.say();
		}
		end = System.currentTimeMillis();
		System.out.println("属性注入："+(end-start));
	}
}
