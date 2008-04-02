package org.jrest.dao.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询 注解
 * @author <a href="mailto:gzyangfan@gmail.com">gzYangfan</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Find {
	
	String query() default "";
	String namedQuery() default "";
	boolean nativeQuery() default false;
	Class<?> resultClass() default void.class;
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.PARAMETER})
	public @interface FirstResult {
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.PARAMETER})
	public @interface MaxResults {
	}
}
