package org.jrest4guice.rest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.commontemplate.core.Context;
import org.commontemplate.engine.Engine;
import org.jrest4guice.client.ModelMap;
import org.jrest4guice.guice.ModuleProviderTemplate;
import org.jrest4guice.rest.annotations.Path;
import org.jrest4guice.rest.annotations.RESTful;
import org.jrest4guice.rest.annotations.RemoteReference;
import org.jrest4guice.rest.helper.ServiceHelper;
import org.jrest4guice.rest.provider.CTLContextProvider;
import org.jrest4guice.rest.provider.CTLEngineProvider;
import org.jrest4guice.rest.provider.HttpRequestProvider;
import org.jrest4guice.rest.provider.HttpResponseProvider;
import org.jrest4guice.rest.provider.HttpSessionProvider;
import org.jrest4guice.rest.provider.ModelMapProvider;
import org.jrest4guice.rest.provider.VelocityContextProvider;
import org.jrest4guice.rest.reader.RequestContentReader;
import org.jrest4guice.rest.reader.RequestContentReaderRegister;
import org.jrest4guice.rest.writer.ResponseWriter;
import org.jrest4guice.rest.writer.ResponseWriterRegister;
import org.jrest4guice.rest.writer.renderer.ViewRender;
import org.jrest4guice.rest.writer.renderer.ViewRenderRegister;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@SuppressWarnings("unchecked")
public class JRest4GuiceModuleProvider extends ModuleProviderTemplate {
	public JRest4GuiceModuleProvider(String... packages) {
		super(packages);
		this.addScanPackages("org.jrest4guice.rest.reader");
		this.addScanPackages("org.jrest4guice.rest.writer");
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
				ResponseWriterRegister writerRegister = new ResponseWriterRegister();
				RequestContentReaderRegister readerRegister = new RequestContentReaderRegister();
				ViewRenderRegister renderRegister = new ViewRenderRegister();

				// 绑定固定的上下文对象
				binder.bind(HttpServletRequest.class).toProvider(
						HttpRequestProvider.class);
				binder.bind(HttpServletResponse.class).toProvider(
						HttpResponseProvider.class);
				binder.bind(HttpSession.class).toProvider(
						HttpSessionProvider.class);
				binder.bind(ModelMap.class).toProvider(ModelMapProvider.class);
				binder.bind(VelocityContext.class).toProvider(VelocityContextProvider.class);
				binder.bind(Engine.class).toProvider(CTLEngineProvider.class);
				binder.bind(Context.class).toProvider(CTLContextProvider.class);

				// 注册所有的Restful服务对象
				ServiceHelper jRestContext = ServiceHelper.getInstance();
				Path annotation;
				String[] uris;
				String mimiType, name;
				String[] mimiTypes;
				Field[] fields;
				RemoteReference remoteServiceAnnotation;
				RESTful resourceAnnotation;
				Class<RemoteReference> remoteServiceClass = RemoteReference.class;
				Class type;
				for (Class clazz : classes) {
					if (clazz.isAnnotationPresent(RESTful.class)) {
						resourceAnnotation = (RESTful)clazz.getAnnotation(RESTful.class);
						if(resourceAnnotation.remoteable()){
							name = resourceAnnotation.name();
							if (name == null || name.trim().equals(""))
								name = clazz.getName();
	
							jRestContext.addRemoteService(name, clazz);
						}

					}

					// 绑定远程服务引用的Provider
					fields = clazz.getDeclaredFields();
					for (Field f : fields) {
						if (f.isAnnotationPresent(remoteServiceClass)) {
							remoteServiceAnnotation = f
									.getAnnotation(remoteServiceClass);
							type = f.getType();
							binder.bind(type).annotatedWith(
									remoteServiceClass).toProvider(
									RemoteServiceProvider.create(type));
						}
					}

					if (clazz.isAnnotationPresent(Path.class)) {
						// 注册Rest服务
						annotation = (Path) clazz.getAnnotation(Path.class);
						uris = annotation.value();
						int index = 0;
						for (String uri : uris)
							jRestContext.addResource(uri, clazz, index++ == 0);
					}else if (clazz.isAnnotationPresent(RESTful.class)) { 
						jRestContext.addResourceByClassMethodInfo(clazz);
					}else if (RequestContentReader.class.isAssignableFrom(clazz)) {
						// 注册RequestContentReader
						try {
							RequestContentReader reader = (RequestContentReader) clazz
									.newInstance();
							readerRegister.registResponseWriter(reader.getContentType(),clazz);
						} catch (Exception e) {
						}
					}else if (ResponseWriter.class.isAssignableFrom(clazz)) {
						// 注册ResponseWriter
						try {
							ResponseWriter writer = (ResponseWriter) clazz
									.newInstance();
							mimiType = writer.getMimeType();
							mimiTypes = mimiType.split(",");
							for (String mt : mimiTypes) {
								if (!mt.trim().equals(""))
									writerRegister.registResponseWriter(mt
											.trim(), clazz);
							}
						} catch (Exception e) {
						}
					} else if (ViewRender.class.isAssignableFrom(clazz)) {
						// 注册ViewRender
						try {
							ViewRender render = (ViewRender) clazz
									.newInstance();
							renderRegister.registViewRender(render
									.getRenderType(), clazz);
						} catch (Exception e) {
						}
					}
				}
			}
		});

		return modules;
	}
}
