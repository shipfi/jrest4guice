package org.jrest4guice.sample.helloworld.resources;

import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.Path;
import org.jrest4guice.rest.annotations.ViewTemplate;
import org.jrest4guice.rest.render.ViewRenderType;

@Path("/helloworld/velocity")
public class HelloWorldWithVelocity {
	
	@Get
	@ViewTemplate(url="/template/helloworld/helloworld.vm",render=ViewRenderType.VELOCITY)
	public String sayHello(){
		return "hello jrest4guice world";
	}
}
