package org.jrest4guice.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( { ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ProduceMime {
	/**
	 * MIME 类型集合. E.g. "application/json，application/xml"
	 */
	String[] value() default MimeType.MIME_OF_JSON;
}
