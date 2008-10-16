package org.jrest4guice.rest.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jrest4guice.rest.writer.renderer.ResultType;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE,ElementType.METHOD})
public @interface PageInfo {
	public static String deaultErrorUrl = "/template/error.ctl";

	String value();
	String type() default ResultType.PLAINTEXT;
}
