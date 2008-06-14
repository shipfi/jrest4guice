package org.jrest4guice.sample.helloworld.resources;

import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.Path;
import org.jrest4guice.rest.annotations.ViewTemplate;
import org.jrest4guice.rest.render.ViewRenderType;

@Path("/helloworld/freemarker")
public class HelloWorldWithFreemarker {
	
	@Get
	@ViewTemplate(url="/template/helloworld/helloworld.ftl",render=ViewRenderType.FREEMARKER)
	public String sayHello(){
		return "hello jrest4guice world";
	}
}
