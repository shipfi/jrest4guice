package org.jrest4guice.jndi;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.jrest4guice.guice.ModuleProviderTemplate;
import org.jrest4guice.jndi.annotations.JndiResource;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
public class JndiGuiceModuleProvider extends ModuleProviderTemplate {
	public JndiGuiceModuleProvider(String... packages) {
		super(packages);
	}

	@Override
	public List<Module> getModules() {
		List<Module> modules = new ArrayList<Module>(0);
		modules.add(new Module() {
			@Override
			public void configure(Binder binder) {
				Field[] fields;
				JndiResource annotation;
				Class type;
				Class<JndiResource> _clazz = JndiResource.class;
				for (Class<?> clazz : classes) {
					fields = clazz.getDeclaredFields();
					for(Field f: fields){
						if(f.isAnnotationPresent(_clazz)){
							annotation = f.getAnnotation(_clazz);
							type = f.getType();
							binder.bind(type).annotatedWith(_clazz).toProvider(JndiProvider.fromJndi(type,annotation.jndi()));
						}
					}
				}			
			}
		});

		return modules;
	}
}
