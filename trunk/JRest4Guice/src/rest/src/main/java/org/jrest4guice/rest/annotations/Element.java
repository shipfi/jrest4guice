package org.jrest4guice.rest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang.ObjectUtils.Null;
import org.jrest4guice.commons.lang.Logic;
import org.jrest4guice.commons.lang.Relation;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Element {
	String name();
	String targetName() default "";
	Logic logic() default Logic.AND;
	Relation relation() default Relation.EQUAL;
	Class<?> dataType() default Null.class;
}
