package ${context.packageName}.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.velocity.app.Velocity;
import org.jrest4guice.rest.JRest4GuiceHelper;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
public class ContextListener implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// 初始化Velocity引擎
		initVelocity(event);
		#if($useJPA)
		JRest4GuiceHelper.useJRest("${context.packageName}")// 使用Rest，并指定要动态扫描注册的包路径
			.useJPA()// 使用JPA
			.init();
		#else
		JRest4GuiceHelper.useJRest("${context.packageName}")// 使用Rest，并指定要动态扫描注册的包路径
			.init();
		#end
		
	}

	private void initVelocity(ServletContextEvent event) {
		String root = event.getServletContext().getRealPath("/");
		Velocity.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, root);
		try {
			Velocity.init();
		} catch (Exception e) {
			throw new RuntimeException("初始化Velocity引擎失败", e);
		}
	}
}
