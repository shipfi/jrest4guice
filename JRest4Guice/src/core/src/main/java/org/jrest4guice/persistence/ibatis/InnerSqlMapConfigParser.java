package org.jrest4guice.persistence.ibatis;

import com.ibatis.sqlmap.engine.builder.xml.SqlMapConfigParser;

class InnerSqlMapConfigParser extends SqlMapConfigParser {
	public InnerSqlMapConfigParser(){
		super();
		this.parser.setValidation(false);
	}
}
