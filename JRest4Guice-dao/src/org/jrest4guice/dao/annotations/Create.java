package org.jrest4guice.dao.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * 创建 注解
 * @author <a href="mailto:gzyangfan@gmail.com">gzYangfan</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Create {

	Class<?> value() default void.class;
}
