package org.jrest4guice.persistence.ibatis;

import java.util.ArrayList;
import java.util.List;

import org.jrest4guice.guice.ModuleProviderTemplate;
import org.jrest4guice.persistence.ibatis.annotations.IbatisDao;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class IbatisGuiceModuleProvider extends ModuleProviderTemplate{
	public IbatisGuiceModuleProvider(String... packages) {
		super(packages);
	}

	@Override
	public List<Module> getModules() {
		List<Module> modules = new ArrayList<Module>(0);
		modules.add(new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bind(SqlMapClient.class).toProvider(
						SqlMapClientProvider.class);

				for (Class clazz : classes) {
					if (clazz.isAnnotationPresent(IbatisDao.class)) {
						SqlMapClientHolder.addIbatisDao(clazz);
					}
				}

				SqlMapClientHolder.initSqlMapClient();
			}
		});
		
		return modules;
	}
}
