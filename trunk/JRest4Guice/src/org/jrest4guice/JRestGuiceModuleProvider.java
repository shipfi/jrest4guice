package org.jrest4guice;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jrest4guice.annotations.Path;
import org.jrest4guice.context.HttpRequestProvider;
import org.jrest4guice.context.HttpResponseProvider;
import org.jrest4guice.context.JRestContext;
import org.jrest4guice.context.ModelMap;
import org.jrest4guice.context.ModelMapProvider;
import org.jrest4guice.core.guice.ModuleProviderTemplate;
import org.jrest4guice.writer.ResponseWriter;
import org.jrest4guice.writer.ResponseWriterRegister;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
@SuppressWarnings("unchecked")
public class JRestGuiceModuleProvider extends ModuleProviderTemplate {
	public JRestGuiceModuleProvider(String... packages) {
		super(packages);
		this.addScanPackages("org.jrest4guice.writer");
	}

	@Override
	public List<Module> getModules() {
		List<Module> modules = new ArrayList<Module>(0);

		modules.add(new Module() {
			@Override
			public void configure(Binder binder) {
				// ===================================================
				// 初始化ResponseWriter
				// ===================================================
				ResponseWriterRegister register = new ResponseWriterRegister();

				// 绑定固定的上下文对象
				binder.bind(HttpServletRequest.class).toProvider(
						HttpRequestProvider.class);
				binder.bind(HttpServletResponse.class).toProvider(
						HttpResponseProvider.class);
				binder.bind(ModelMap.class).toProvider(ModelMapProvider.class);

				// 注册所有的Restful服务对象
				JRestContext jRestContext = JRestContext.getInstance();
				Path annotation;
				String[] uris;
				String mimiType;
				String[] mimiTypes;
				for (Class clazz : classes) {
					if (clazz.isAnnotationPresent(Path.class)) {
						annotation = (Path) clazz
								.getAnnotation(Path.class);
						uris = annotation.value();
						int index = 0;
						for (String uri : uris)
							jRestContext.addResource(uri, clazz,index++==0);
					} else if (ResponseWriter.class.isAssignableFrom(clazz)) {
						// 注册ResponseWriter
						try {
							ResponseWriter writer = (ResponseWriter) clazz
									.newInstance();
							mimiType = writer.getMimeType();
							mimiTypes = mimiType.split(",");
							for (String mt : mimiTypes) {
								if (!mt.trim().equals(""))
									register.registResponseWriter(mt.trim(),
											clazz);
							}
						} catch (Exception e) {
						}
					}
				}
			}
		});

		return modules;
	}
}
