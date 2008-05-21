package org.jrest4guice.core.exception;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 * 
 */
public class UserNotLoginException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2161586652454551900L;

	public UserNotLoginException(String message) {
		super(message);
	}
}
