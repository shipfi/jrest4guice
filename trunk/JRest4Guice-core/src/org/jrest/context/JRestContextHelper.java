package org.jrest.context;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.cnoss.guice.GuiceContext;
import org.cnoss.util.ClassPathScanner;
import org.cnoss.util.ClassPathScanner.ClassFilter;
import org.jrest.annotation.JndiResource;
import org.jrest.annotation.Restful;

import com.google.inject.Binder;
import com.google.inject.Module;

class JRestContextHelper {
	public void constructGuiceInector(ContextConfig config) throws Exception{
		List<Class<?>> resources = new ArrayList<Class<?>>(0);
		// 获取要扫描的资源所对应的包路径
		String resource_package = config.getInitParameter("resource-package");
		String[] packages = null;
		if (resource_package != null && !resource_package.trim().equals("")) {
			packages = resource_package.split(",");
			for (String packageName : packages)
				this.scanResource(resources, packageName);
		}

		// 加载Guice的模块
		String guiceModuleClass = config.getInitParameter("GuiceModuleClass");
		final List<Module> modules = new ArrayList<Module>(0);
		modules.add(new JRestModule());
		modules.add(this.generateGuiceProviderModule(resources));

		try {
			if (guiceModuleClass != null && !guiceModuleClass.trim().equals("")) {
				modules.add((Module) Class.forName(guiceModuleClass)
						.newInstance());
			}
		} catch (Exception e) {
			throw new Exception("初始化 JRestContextHelper 错误：\n"
					+ e.getMessage());
		}
		
		List<String> packageList = new ArrayList<String>();
		
		CollectionUtils.addAll(packageList, packages);

		//初始化Guice上下文
		GuiceContext.getInstance().init(modules, packageList);
		
		// 注册资源
		this.registResource(resources);
	}

	private void scanResource(List<Class<?>> resources, String packageName) {
		List<Class<?>> list = new ClassPathScanner(packageName,
				new ClassFilter() {
					public boolean accept(Class<?> clazz) {
						return clazz.isAnnotationPresent(Restful.class);
					}
				}).scan();

		resources.addAll(list);
	}

	/**
	 * 生成Guice的Provider模块
	 * 
	 * @modelMap resources
	 * @return
	 */
	private Module generateGuiceProviderModule(List<Class<?>> resources) {
		final Set<JndiServiceInfo> jndiServiceInfos = new HashSet<JndiServiceInfo>(
				0);
		JndiResource annotation;
		for (Class<?> clazz : resources) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				annotation = field.getAnnotation(JndiResource.class);
				if (annotation != null) {
					jndiServiceInfos.add(new JndiServiceInfo(field.getType(),
							annotation.jndi()));
				}
			}
		}

		Module module = new Module() {
			@Override
			public void configure(Binder binder) {
				Class serviceClass;
				for (JndiServiceInfo info : jndiServiceInfos) {
					serviceClass = info.getServiceClass();
					binder.bind(serviceClass).toProvider(
							JndiProvider.fromJndi(serviceClass, info
									.getJndiName()));
				}
			}
		};

		return module;
	}

	private void registResource(List<Class<?>> resources) {
		for (Class<?> clazz : resources) {
			// 获取注解中的URI映射规则
			// 一个资源可以映射到多个URL上
			String[] uri = clazz.getAnnotation(Restful.class).uri();
			for (String s : uri) {
				// 注册此REST资源
				JRestContext.getInstance().addResource(s, clazz);
			}
		}
	}
}
