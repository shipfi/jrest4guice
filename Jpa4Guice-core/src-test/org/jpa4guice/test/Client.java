package org.jpa4guice.test;

import org.cnoss.guice.GuiceContext;

public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final GuiceContext instance = GuiceContext.getInstance();
		instance.init(null,"org.jpa4guice");
		ContactService service = instance.getInstance(ContactService.class);
		
		service.createContact();
		service.listContact();
	}
}
