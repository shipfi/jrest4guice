package org.jrest4guice.interceptor;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.jrest4guice.guice.ModuleProviderTemplate;
import org.jrest4guice.interceptor.annotations.Interceptor;
import org.jrest4guice.interceptor.annotations.Interceptors;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@SuppressWarnings("unchecked")
public class InterceptorGuiceModuleProvider extends ModuleProviderTemplate {
	public InterceptorGuiceModuleProvider(String... packages) {
		super(packages);
	}

	@Override
	public List<Module> getModules() {
		List<Module> modules = new ArrayList<Module>(0);
		modules.add(new Module() {
			@Override
			public void configure(Binder binder) {
				AbstractMatcher<Class> classMatcher = new AbstractMatcher<Class>() {
					public boolean matches(Class clazz) {
						return clazz.isAnnotationPresent(Interceptors.class)||clazz.isAnnotationPresent(Interceptor.class);
					}

					public String toString() {
						return "annotatedWith("
								+ Interceptors.class.getSimpleName()
								+ ".class)";
					}
				};

				MethodNotMatcher methodNotMatcher = new MethodNotMatcher();

				for (Class clazz : classes) {
					//==============================================================
					// 处理类级别的拦截器
					//==============================================================
					if (clazz.isAnnotationPresent(Interceptor.class)) {
						Interceptor[] interceptors = { (Interceptor) clazz
								.getAnnotation(Interceptor.class) };
						bindInterceptor(binder, classMatcher, methodNotMatcher,interceptors);
					}else if (clazz.isAnnotationPresent(Interceptors.class)) {
						bindInterceptor(binder, classMatcher, methodNotMatcher,
								((Interceptors) clazz
										.getAnnotation(Interceptors.class))
										.value());
					}

					//==============================================================
					// 处理类方法级别的拦截器
					//==============================================================
					Method[] methods = clazz.getDeclaredMethods();
					for (Method m : methods) {
						if (m.isAnnotationPresent(Interceptors.class)) {
							bindInterceptor(binder, new ClassMatcher(clazz),
									Matchers.annotatedWith(Interceptors.class),
									((Interceptors) m
											.getAnnotation(Interceptors.class))
											.value());
						}

						if (m.isAnnotationPresent(Interceptor.class)) {
							Interceptor[] interceptors = { (Interceptor) m
									.getAnnotation(Interceptor.class) };
							bindInterceptor(binder, new ClassMatcher(clazz),
									Matchers.annotatedWith(Interceptor.class),
									interceptors);
						}
					}
				}
			}
		});

		return modules;
	}

	private void bindInterceptor(Binder binder, Matcher classMatcher,
			Matcher methodMatcher, Interceptor... interceptors) {
		List<MethodInterceptor> methodInterceptors = new ArrayList<MethodInterceptor>();
		Object obj;
		if (interceptors != null) {
			for (Interceptor itr : interceptors) {
				try {
					obj = itr.value().newInstance();
					if (obj instanceof MethodInterceptor) {
						methodInterceptors.add((MethodInterceptor) obj);
					}
				} catch (Exception e) {
				}
			}

			if (methodInterceptors.size() > 0) {
				binder.bindInterceptor(classMatcher, methodMatcher,
						methodInterceptors.toArray(new MethodInterceptor[] {}));
			}
		}
	}

	class ClassMatcher extends AbstractMatcher<Class> {
		private Class clazz;

		public ClassMatcher(Class clazz) {
			this.clazz = clazz;
		}

		public boolean matches(Class clazz) {
			return this.clazz.equals(clazz);
		}

		public String toString() {
			return "InterceptorGuiceModuleProvider-ClassMatcher";
		}
	};

	class MethodNotMatcher extends AbstractMatcher<AnnotatedElement> {
		public boolean matches(AnnotatedElement element) {
			return !(element.isAnnotationPresent(Interceptors.class) || element
					.isAnnotationPresent(Interceptor.class));
		}

		public String toString() {
			return "InterceptorGuiceModuleProvider-MethodNotMatcher";
		}
	};
}
