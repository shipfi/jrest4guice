package org.jrest4guice.rest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;


/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@BindingAnnotation
@Target(ElementType.FIELD)
public @interface RemoteService {
}
