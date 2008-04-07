package org.jrest.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jrest.core.guice.AbstractGuiceModuleProvider;
import org.jrest.rest.annotation.Restful;
import org.jrest.rest.http.HttpRequestProvider;
import org.jrest.rest.http.HttpResponseProvider;
import org.jrest.rest.http.JRestContext;
import org.jrest.rest.http.ModelMap;
import org.jrest.rest.http.ModelMapProvider;

import com.google.inject.Binder;
import com.google.inject.Module;

@SuppressWarnings("unchecked")
public class JRestGuiceModuleProvider extends AbstractGuiceModuleProvider {
	public JRestGuiceModuleProvider() {
		this(null);
	}

	public JRestGuiceModuleProvider(String[] packages) {
		if (packages != null)
			this
					.setScanPackageList(new HashSet<String>(Arrays
							.asList(packages)));
		else
			this.setScanPackageList(null);
	}

	@Override
	public List<Module> getModules() {
		List<Module> modules = new ArrayList<Module>(0);
		modules.add(new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bind(HttpServletRequest.class).toProvider(
						HttpRequestProvider.class);
				binder.bind(HttpServletResponse.class).toProvider(
						HttpResponseProvider.class);
				binder.bind(ModelMap.class).toProvider(ModelMapProvider.class);

				JRestContext jRestContext = JRestContext.getInstance();
				Restful annotation;
				String[] uris;
				for (Class clazz : classes) {
					if (clazz.isAnnotationPresent(Restful.class)) {
						annotation = (Restful) clazz
								.getAnnotation(Restful.class);
						uris = annotation.uri();
						for (String uri : uris)
							jRestContext.addResource(uri, clazz);
					}
				}
			}
		});
		return modules;
	}
}
