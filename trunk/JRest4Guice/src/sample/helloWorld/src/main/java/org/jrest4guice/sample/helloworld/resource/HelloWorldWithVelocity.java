package org.jrest4guice.sample.helloworld.resource;

import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.PageFlow;
import org.jrest4guice.rest.annotations.PageInfo;
import org.jrest4guice.rest.annotations.Path;
import org.jrest4guice.rest.render.ResultType;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@Path("/HelloWorld/velocity")
public class HelloWorldWithVelocity {

	@Get
	@PageFlow(success = @PageInfo(value = "/template/HelloWorld.vm"),type=ResultType.VELOCITY)
	public String sayHello() {
		return "hello JRest4Guice world";
	}
}
