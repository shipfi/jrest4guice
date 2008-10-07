package org.jrest4guice.sample.helloworld.resource;

import org.jrest4guice.rest.annotations.Cache;
import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.Path;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@Path("/HelloWorld")
public class HelloWorldSimple {
	
	@Get
	@Cache
	public String sayHello(){
		return "hello JRest4Guice world";
	}
}
