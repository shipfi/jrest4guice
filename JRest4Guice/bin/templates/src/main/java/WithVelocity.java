package ${context.packageName}.resource;

import org.jrest4guice.rest.annotations.RESTful;
import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.PageFlow;
import org.jrest4guice.rest.annotations.PageInfo;
import org.jrest4guice.rest.annotations.Path;
import org.jrest4guice.rest.annotations.RESTful;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@RESTful(name = "${context.projectName}WithVelocity")
@Path("/${context.projectName}/velocity")
public class ${context.projectName}WithVelocity {
	
	@Get
	@PageFlow(success = @PageInfo(url = "/template/${context.projectName}.vm"))
	public String sayHello(){
		return "hello ${context.projectName} world";
	}
}
