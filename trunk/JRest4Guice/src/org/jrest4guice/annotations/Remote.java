package org.jrest4guice.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Remote {
	public static final String REMOTE_SERVICE_PREFIX = "__remote_service__"; 
	public static final String REMOTE_SERVICE_NAME_KEY = "__remote_service_name__"; 
	public static final String REMOTE_SERVICE_METHOD_INDEX_KEY = "__remote_service_method_index_"; 
	String value() default "";
}
