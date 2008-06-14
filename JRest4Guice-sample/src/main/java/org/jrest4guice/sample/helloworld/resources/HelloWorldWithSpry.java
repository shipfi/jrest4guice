package org.jrest4guice.sample.helloworld.resources;

import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.Path;
import org.jrest4guice.rest.annotations.ViewTemplate;
import org.jrest4guice.rest.render.ViewRenderType;

@Path("/helloworld/spry")
public class HelloWorldWithSpry {
	
	@Get
	@ViewTemplate(url="/template/helloworld/helloworld.html",render=ViewRenderType.SPRY)
	public String sayHello(){
		return "hello jrest4guice world";
	}
}
