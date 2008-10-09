package org.jrest4guice.persistence.ibatis;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ibatis.sqlmap.client.SqlMapClient;


/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class SqlMapClientProvider implements Provider<SqlMapClient> {
	@Inject
	private SqlMapClientHolder sqlMapClientHolder;
    
	public SqlMapClient get() {
        return sqlMapClientHolder.getSqlMapClient();
    }
}
