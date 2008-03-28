package org.jrest.ioc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.inject.Binder;
import com.google.inject.Module;

public class JRestModule implements Module {
	@Override
	public void configure(Binder binder) {
		try {
			binder.bind(HttpServletRequest.class).toProvider(
					HttpRequestProvider.class);
			binder.bind(HttpServletResponse.class).toProvider(
					HttpResponseProvider.class);
			binder.bind(ModelMap.class).toProvider(
					ModelMapProvider.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
