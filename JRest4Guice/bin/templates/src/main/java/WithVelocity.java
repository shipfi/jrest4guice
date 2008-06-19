package ${context.packageName}.resource;

import org.jrest4guice.rest.annotations.Get;
import org.jrest4guice.rest.annotations.Path;
import org.jrest4guice.rest.annotations.ViewTemplate;
import org.jrest4guice.rest.render.ViewRenderType;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@Path("/${context.projectName}/velocity")
public class ${context.projectName}WithVelocity {
	
	@Get
	@ViewTemplate(url="/template/${context.projectName}.vm",render=ViewRenderType.VELOCITY)
	public String sayHello(){
		return "hello ${context.projectName} world";
	}
}
