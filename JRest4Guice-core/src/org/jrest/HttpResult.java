package org.jrest;

import net.sf.json.JSONObject;

public class HttpResult {
	private String errorType;
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

	public HttpResult(String errorCode, String errorMessage) {
		super();
		this.errorType = errorCode;
		this.errorMessage = errorMessage;
	}

	public HttpResult(Object content) {
		super();
		this.content = content;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorCode) {
		this.errorType = errorCode;
	}

	public String getErrorMessage() {
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

	public String toJson() {
		return JSONObject.fromObject(this).toString();
	}

	public static HttpResult createSuccessHttpResult(int resultCount,
			int pageCount, Object content) {
		return new HttpResult(content);
	}

	public static HttpResult createSuccessfulHttpResult(Object content) {
		return new HttpResult(content);
	}

	public static HttpResult createFailedHttpResult(String errorCode,
			String errorMessage) {
		return new HttpResult(errorCode, errorMessage);
	}
}
