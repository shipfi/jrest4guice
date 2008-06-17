package org.jrest4guice.security.exception;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
public class AccessDeniedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 319089011662988364L;

	public AccessDeniedException(String message) {
		super(message);
	}
}
