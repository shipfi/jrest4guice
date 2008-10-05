package org.jrest4guice.rest.exception;

public class ServiceNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1658037695165025547L;
	
	public ServiceNotFoundException(String msg){
		super(msg);
	}

}
