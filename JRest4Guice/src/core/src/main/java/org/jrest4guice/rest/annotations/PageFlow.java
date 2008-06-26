package org.jrest4guice.rest.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来描述页面流程转向
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PageFlow {
	/**
	 * 成功时要转向的下一个流程（通常是一个页面）
	 */
	PageInfo success();

	/**
	 * 出错时要转向的下一个流程（通常是一个页面）
	 */
	PageInfo error() default @PageInfo(url = PageInfo.deaultErrorUrl);
}
