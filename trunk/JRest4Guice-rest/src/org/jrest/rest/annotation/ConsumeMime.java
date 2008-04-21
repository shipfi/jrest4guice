package org.jrest.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( { ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ConsumeMime {
	/**
	 * MIME 类型集合. E.g. {"image/jpeg","image/gif"}
	 */
	String[] value() default "*/*";
}
