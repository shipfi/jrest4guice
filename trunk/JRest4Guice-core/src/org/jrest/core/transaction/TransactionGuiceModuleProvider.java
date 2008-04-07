package org.jrest.core.transaction;

import java.util.ArrayList;
import java.util.List;

import org.jrest.core.guice.AbstractGuiceModuleProvider;
import org.jrest.core.transaction.annotations.Transactional;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;

public class TransactionGuiceModuleProvider extends AbstractGuiceModuleProvider{
	@Override
	public List<Module> getModules() {
		List<Module> modules = new ArrayList<Module>(0);
		modules.add(new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bindInterceptor(Matchers.any(), Matchers
						.annotatedWith(Transactional.class),
						new JpaLocalTransactionInterceptor());
			}
		});

		return modules;
	}
}
