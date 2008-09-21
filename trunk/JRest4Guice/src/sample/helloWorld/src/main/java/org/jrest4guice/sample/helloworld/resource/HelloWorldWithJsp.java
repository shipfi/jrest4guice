package org.jrest4guice.sample.helloworld.resource;

import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.PageFlow;
import org.jrest4guice.rest.annotations.PageInfo;
import org.jrest4guice.rest.annotations.Path;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@Path("/HelloWorld/jsp")
public class HelloWorldWithJsp {

	@Get
	@PageFlow(success = @PageInfo(value = "/template/HelloWorld.jsp"))
	public String sayHello() {
		return "hello JRest4Guice world";
	}
}
