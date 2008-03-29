package org.jpa4guice;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

import org.cnoss.guice.GuiceSupportAble;
import org.jpa4guice.annotation.Transactional;
import org.jpa4guice.interceptor.TransactionInterceptor;
import org.jpa4guice.transaction.JpaBindingSupport;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;

public class JpaGuiceContextSupport implements GuiceSupportAble{
	@Override
	public List<Module> getModules() {
		List<Module> modules = new ArrayList<Module>(0);
		modules.add(new Module() {
			@Override
			public void configure(Binder binder) {
				Matcher<AnnotatedElement> annotatedWith = Matchers
						.annotatedWith(Transactional.class);
				binder.bindInterceptor(Matchers.any(), annotatedWith,
						new TransactionInterceptor());
				JpaBindingSupport.addBindings(binder);
			}
		});
		
		return modules;
	}
}
