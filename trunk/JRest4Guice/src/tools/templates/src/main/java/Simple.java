package ${context.packageName}.resource;

import org.jrest4guice.rest.annotations.RESTful;
import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.Path;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@RESTful(name = "${context.projectName}")
@Path("/${context.projectName}")
public class ${context.projectName}Simple {
	
	@Get
	public String sayHello(){
		return "hello ${context.projectName} world";
	}
}
