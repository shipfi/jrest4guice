package org.jrest4guice.rest.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RESTful {
	public static final String REMOTE_SERVICE_PREFIX = "__remote_service__"; 
	public static final String REMOTE_SERVICE_NAME_KEY = "__remote_service_name__"; 
	public static final String REMOTE_SERVICE_METHOD_INDEX_KEY = "__remote_service_method_index_"; 

	public static final String METHOD_OF_GET = "get";
	public static final String METHOD_OF_POST = "post";
	public static final String METHOD_OF_PUT = "put";
	public static final String METHOD_OF_DELETE = "delete";
	
	
	String name();
	Scope scope() default Scope.Session;
	boolean remoteable() default false;
}
