package org.jrest4guice.sample.helloworld.resources;

import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.Path;

@Path("/helloworld")
public class HelloWorld {
	
	@Get
	public String sayHello(){
		return "hello jrest4guice world";
	}
}
