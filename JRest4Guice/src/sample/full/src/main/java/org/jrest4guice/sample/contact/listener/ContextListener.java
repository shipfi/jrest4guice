package org.jrest4guice.sample.contact.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.velocity.app.Velocity;
import org.commontemplate.engine.Engine;
import org.commontemplate.standard.ConfigurationSettings;
import org.commontemplate.tools.PropertiesConfigurationLoader;
import org.jrest4guice.rest.RestContextManager;
import org.jrest4guice.rest.helper.JRest4GuiceHelper;
import org.jrest4guice.rest.reader.MultipartFormDataContentRader;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;

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
		try {
			// 初始化Velocity引擎
			this.initVelocity(event);
			// 初始化CTL引擎
			this.initCTL(event);

			JRest4GuiceHelper.useJRest("org.jrest4guice.sample")// 使用Rest，并指定要动态扫描注册的包路径
					.useJPA()// 使用JPA
					.useSecurity()// 使用JAAS
					.useCache()//打开缓存客户端的功能
//					.enableCustomInterceptor("org.jrest4guice.sample")//打开自定义的拦截器支持，允许通过@Interceptors来支持自定义的拦截器
					.addUserModule(new Module(){
						@Override
						public void configure(Binder binder) {
							binder.bindConstant().annotatedWith(Names.named(MultipartFormDataContentRader.FILE_SIZE_MAX)).to(1024 * 1024*10);
							binder.bindConstant().annotatedWith(Names.named(MultipartFormDataContentRader.SIZE_MAX)).to(1024 * 1024*100);
						}
					})
					.init();
		} catch (Exception e) {
			System.out.println("初始化ContextListener错误");
			e.printStackTrace();
		}
	}

	private void initCTL(ServletContextEvent event) {
		// 配置并建造引擎 (Engine是内同步线程安全的，可单例重用) 
		try {
			ConfigurationSettings config = PropertiesConfigurationLoader
					.loadConfiguration("ctl.properties");
			Engine engine = new Engine(config);
			RestContextManager.setCTLEngine(engine);
		} catch (Exception e) {
			throw new RuntimeException("初始化CTL引擎失败", e);
		}
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
