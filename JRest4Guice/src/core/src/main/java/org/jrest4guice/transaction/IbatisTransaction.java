package org.jrest4guice.transaction;

import com.ibatis.sqlmap.client.SqlMapClient;

public class IbatisTransaction {
	private boolean active = false;
	private SqlMapClient client;
	
	public IbatisTransaction(SqlMapClient client){
		this.client = client;
	}

	public void begin() throws Exception {
		if(this.isActive())
			return;
		
		this.active = true;
		this.client.startTransaction();
	}

	public void commit() throws Exception {
		this.client.commitTransaction();
	}

	public void end() throws Exception {
		this.client.endTransaction();
		this.active = false;
	}

	public boolean isActive() throws Exception {
		return this.active;
	}
}
