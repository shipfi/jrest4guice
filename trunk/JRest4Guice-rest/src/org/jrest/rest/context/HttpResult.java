package org.jrest.rest.context;

import net.sf.json.JSONObject;

import org.jrest.core.persist.jpa.Page;

public class HttpResult {
	private String errorMessage;

	/**
	 * 所有记录的总数
	 */
	private int resultCount;
	/**
	 * 记录的页数
	 */
	private int pageCount;

	/**
	 * 结果的内容
	 */
	private Object content;

	public HttpResult(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	public int getResultCount() {
		return resultCount;
	}

	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public HttpResult(Object content) {
		super();
		this.content = content;
	}

	public String getErrorMessage() {
		if (errorMessage != null && errorMessage.trim().equals(""))
			return null;
		else
			return errorMessage;
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
		if(this.content != null)
			return this.content.toString();
		else
			return this.errorMessage;
	}

	public String toXML() {
		//TODO 实现对象的XML串行化
		return JSONObject.fromObject(this).toString();
	}

	public String toJson() {
		return JSONObject.fromObject(this).toString();
	}

	public static HttpResult createSuccessHttpResult(int resultCount,
			int pageCount, Object content) {
		return new HttpResult(content);
	}

	public static HttpResult createHttpResult(Object content) {
		if (content instanceof Exception)
			return new HttpResult(((Exception) content).getMessage());
		HttpResult httpResult = new HttpResult(content);
		if (content instanceof Page){
			Page page = (Page)content;
			httpResult.setContent(page.getResult());
			httpResult.setResultCount((int)page.getTotalCount());
			httpResult.setPageCount((int)page.getTotalPageCount());
		}
		return httpResult;
	}
}
