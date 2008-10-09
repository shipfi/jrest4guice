package org.jrest4guice.persistence.ibatis.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang.ObjectUtils.Null;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Statement {
	String id() default "";
	Class<?> parameterClass() default Null.class;
	String parameterMap() default "";
	Class<?> resltClass() default Null.class;
	String resltMap() default "";
	String sql();
	String cacheModel() default "";
	String xmlResultName() default "";
}
