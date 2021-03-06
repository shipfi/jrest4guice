package org.jrest4guice.rest;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.xml.XMLSerializer;

import org.hibernate.validator.InvalidValue;
import org.jrest4guice.client.Page;
import org.jrest4guice.rest.commons.json.DateJsonValueProcessor;
import org.jrest4guice.rest.commons.json.JsonConfigFactory;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
public class ServiceResult {
	
	public static final String INVALID_VALUE_KEY = "_$_invalidvalue_key_$__";
	
	private String errorType;

	private String errorMessage;
	
	/**
	 * 对Hibernate validator的支持
	 */
	private InvalidValue[] invalidValues;
	
	private Map<String, InvalidValue> invalidValueMap;

	private Object invalidBean;
	
	private Boolean inChain = null;
	
	/**
	 * 所有记录的总数
	 */
	private Integer resultCount = null;
	/**
	 * 记录的页数
	 */
	private Integer pageCount = null;

	/**
	 * 当前页码
	 */
	private Integer pageIndex = null;

	/**
	 * 结果的内容
	 */
	private Object content;

	public ServiceResult() {
	}

	public ServiceResult(String errorType, String errorMessage) {
		super();
		this.errorType = errorType;
		this.errorMessage = errorMessage;
	}

	public Integer getResultCount() {
		return resultCount;
	}

	public void setResultCount(Integer resultCount) {
		this.resultCount = resultCount;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public ServiceResult(Object content) {
		super();
		this.content = content;
	}

	public String getErrorMessage() {
		if (errorMessage != null && errorMessage.trim().equals(""))
			return null;
		else
			return errorMessage;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public String toTextPlain() {
		String result = null;
		if (this.content != null)
			result = this.content.toString();
		else
			result = this.errorMessage;
		
		if(result == null)
			result = "";
		return result;
	}

	public String toXML() {
		return new XMLSerializer().write(JSONObject.fromObject(this,
				JsonConfigFactory.createJsonConfig(this.content)));
	}

	public String toJson() {
		JsonConfig jsonConfig = JsonConfigFactory.createJsonConfig(this.content);
		
		jsonConfig.registerJsonValueProcessor(Date.class,new DateJsonValueProcessor());
		jsonConfig.registerJsonValueProcessor(Timestamp.class,new DateJsonValueProcessor());  

		JsonConfigFactory.filteExcludes(this, jsonConfig);
		return JSONObject.fromObject(this,
				jsonConfig).toString();
	}

	public static ServiceResult createSuccessHttpResult(Integer resultCount,
			Integer pageCount, Object content) {
		return new ServiceResult(content);
	}

	public static ServiceResult createHttpResult(Object content) {
		if (content instanceof Exception) {
			Exception exception = (Exception) content;
			return new ServiceResult(exception.getClass().getName(), exception
					.getMessage());
		}
		ServiceResult jRestResult = new ServiceResult(content);
		if (content instanceof Page) {
			Page<?> page = (Page<?>) content;
			jRestResult.setContent(page.getResult());
			jRestResult.setResultCount(new Integer((int)page.getTotalCount()));
			jRestResult.setPageCount(new Integer((int)page.getTotalPageCount()));
			jRestResult.setPageIndex(page.getCurrentPageNo());
		}
		return jRestResult;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public InvalidValue[] getInvalidValues() {
		return invalidValues;
	}

	public void setInvalidValues(InvalidValue[] invalidValues) {
		this.invalidValues = invalidValues;
		if(this.invalidValues != null && this.invalidValues.length>0){
			this.invalidBean = this.invalidValues[0].getBean();
			this.content = this.invalidBean;
			
			this.invalidValueMap = new HashMap<String, InvalidValue>(this.invalidValues.length);
			for(InvalidValue value :this.invalidValues){
				this.invalidValueMap.put(value.getPropertyName(), value);
			}
		}
	}

	public Map<String, InvalidValue> getInvalidValueMap() {
		return invalidValueMap;
	}

	public Object getInvalidBean() {
		return invalidBean;
	}

	public Boolean isInChain() {
		return inChain;
	}

	public void setInChain(Boolean inChain) {
		this.inChain = inChain;
	}
}
