package org.cnoss.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cnoss.core.guice.ModuleProviderTemplate;
import org.cnoss.rest.annotation.Restful;
import org.cnoss.rest.context.HttpRequestProvider;
import org.cnoss.rest.context.HttpResponseProvider;
import org.cnoss.rest.context.JRestContext;
import org.cnoss.rest.context.ModelMap;
import org.cnoss.rest.context.ModelMapProvider;
import org.cnoss.rest.writer.ResponseWriter;
import org.cnoss.rest.writer.ResponseWriterRegister;

import com.google.inject.Binder;
import com.google.inject.Module;

@SuppressWarnings("unchecked")
public class JRestGuiceModuleProvider extends ModuleProviderTemplate {
	public JRestGuiceModuleProvider(String... packages) {
		super(packages);
		this.addScanPackages("org.cnoss.rest.writer");
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
				Restful annotation;
				String[] uris;
				String mimiType;
				String[] mimiTypes;
				for (Class clazz : classes) {
					if (clazz.isAnnotationPresent(Restful.class)) {
						annotation = (Restful) clazz
								.getAnnotation(Restful.class);
						uris = annotation.uri();
						for (String uri : uris)
							jRestContext.addResource(uri, clazz);
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
