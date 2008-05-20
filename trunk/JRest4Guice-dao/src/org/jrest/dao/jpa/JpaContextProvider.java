package org.jrest.dao.jpa;

import org.cnoss.core.guice.GuiceContext;
import org.jrest.dao.actions.ActionContextProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

/**
 * <code>JpaContext</code>提供者<br>
 * 注意:该提供者提供的<code>JpaContext</code>自身实现事务，主要用于脱离事务层的DAO层独立使用。
 * @author <a href="mailto:gzyangfan@gmail.com">gzYangfan</a>
 */
public class JpaContextProvider implements ActionContextProvider<JpaContext> {
	
	@Override
	public JpaContext get() {
		JpaContext context = new JpaContext();
		GuiceContext.getInstance().injectorMembers(context);
		context.setWithoutService(true);
		return context;
	}
	
	@Override
	public Module getModule() {
		return new AbstractModule() {
			@Override
			protected void configure() {
				bind(JpaContext.class).toProvider(JpaContextProvider.class);
			}
		};
	}
}
