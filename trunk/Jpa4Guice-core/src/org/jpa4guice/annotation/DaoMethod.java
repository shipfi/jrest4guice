package org.jpa4guice.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DaoMethod {
	DaoMethodType type() default DaoMethodType.NONE;
	String query() default "";
	String namedQuery() default "";
}
