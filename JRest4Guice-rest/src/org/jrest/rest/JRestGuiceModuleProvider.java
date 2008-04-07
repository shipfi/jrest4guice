package org.jrest.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jrest.core.guice.AbstractGuiceModuleProvider;
import org.jrest.rest.http.HttpRequestProvider;
import org.jrest.rest.http.HttpResponseProvider;
import org.jrest.rest.http.ModelMap;
import org.jrest.rest.http.ModelMapProvider;

import com.google.inject.Binder;
import com.google.inject.Module;

public class JRestGuiceModuleProvider extends AbstractGuiceModuleProvider {
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
				binder.bind(ModelMap.class).toProvider(
						ModelMapProvider.class);
			}
		});
		return modules;
	}
}
