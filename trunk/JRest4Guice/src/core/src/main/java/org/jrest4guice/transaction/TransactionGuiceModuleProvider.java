package org.jrest4guice.transaction;

import java.util.ArrayList;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.guice.ModuleProviderTemplate;
import org.jrest4guice.transaction.annotations.Transactional;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class TransactionGuiceModuleProvider extends ModuleProviderTemplate{
	private MethodInterceptor localTransactionInterceptor;
	public TransactionGuiceModuleProvider(MethodInterceptor localTransactionInterceptor){
		this.localTransactionInterceptor = localTransactionInterceptor;
	}
	
	@Override
	public List<Module> getModules() {
		List<Module> modules = new ArrayList<Module>(0);
		modules.add(new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bindInterceptor(Matchers.any(), Matchers
						.annotatedWith(Transactional.class),
						TransactionGuiceModuleProvider.this.localTransactionInterceptor);
			}
		});

		return modules;
	}
}
