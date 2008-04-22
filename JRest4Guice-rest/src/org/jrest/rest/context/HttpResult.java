package org.jrest.rest.context;

import net.sf.json.JSONObject;

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
		return this.content.toString();
	}

	public String toXML() {
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
		else
			return new HttpResult(content);
	}
}
