package org.jrest4guice.persistence.ibatis;

public class SqlMapping {
	private String parameterMap;
	private String resultMap;
	private String statement;
	public String getParameterMap() {
		return parameterMap;
	}
	public void setParameterMap(String parameterMap) {
		this.parameterMap = parameterMap;
	}
	public String getResultMap() {
		return resultMap;
	}
	public void setResultMap(String resultMap) {
		this.resultMap = resultMap;
	}
	public String getStatement() {
		return statement;
	}
	public void setStatement(String statement) {
		this.statement = statement;
	}
}
