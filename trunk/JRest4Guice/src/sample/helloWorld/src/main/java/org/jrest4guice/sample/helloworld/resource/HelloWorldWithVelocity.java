package org.jrest4guice.sample.helloworld.resource;

import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.Path;
import org.jrest4guice.rest.annotations.ViewTemplate;
import org.jrest4guice.rest.render.ViewRenderType;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@Path("/HelloWorld/velocity")
public class HelloWorldWithVelocity {
	
	@Get
	@ViewTemplate(url="/template/HelloWorld.vm",render=ViewRenderType.VELOCITY)
	public String sayHello(){
		return "hello JRest4Guice world";
	}
}
