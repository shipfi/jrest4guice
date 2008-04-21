package org.jrest.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jrest.core.guice.ModuleProviderTemplate;
import org.jrest.rest.annotation.Restful;
import org.jrest.rest.http.HttpRequestProvider;
import org.jrest.rest.http.HttpResponseProvider;
import org.jrest.rest.http.JRestContext;
import org.jrest.rest.http.ModelMap;
import org.jrest.rest.http.ModelMapProvider;
import org.jrest.rest.writer.ResponseWriter;
import org.jrest.rest.writer.ResponseWriterRegister;

import com.google.inject.Binder;
import com.google.inject.Module;

@SuppressWarnings("unchecked")
public class JRestGuiceModuleProvider extends ModuleProviderTemplate {
	public JRestGuiceModuleProvider(String... packages) {
		super(packages);
		this.addScanPackages("org.jrest.rest.writer");
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
