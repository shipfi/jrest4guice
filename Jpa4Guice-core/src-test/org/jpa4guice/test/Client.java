package org.jpa4guice.test;

import java.util.Arrays;

import org.cnoss.guice.GuiceContext;

public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final GuiceContext instance = GuiceContext.getInstance();
		
		instance.init(null,Arrays.asList(new String[]{"org.jpa4guice"}));
		ContactService service = instance.getInstance(ContactService.class);
		
		service.createContact();
		service.listContact();
	}
}
