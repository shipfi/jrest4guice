package org.jrest4guice.client;

import net.sf.json.JSONObject;

import org.jrest4guice.core.client.Page;


/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 * 
 */
public class JRestResult {
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
	 * 当前页码
	 */
	private int pageIndex;

	/**
	 * 结果的内容
	 */
	private Object content;

	public JRestResult(){
	}
	
	public JRestResult(String errorType, String errorMessage) {
		super();
		this.errorType = errorType;
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

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public JRestResult(Object content) {
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
		if (this.content != null)
			return this.content.toString();
		else
			return this.errorMessage;
	}

	public String toXML() {
		// TODO 实现对象的XML串行化
		return JSONObject.fromObject(this).toString();
	}

	public String toJson() {
		return JSONObject.fromObject(this).toString();
	}

	public static JRestResult createSuccessHttpResult(int resultCount,
			int pageCount, Object content) {
		return new JRestResult(content);
	}

	public static JRestResult createHttpResult(Object content) {
		if (content instanceof Exception) {
			Exception exception = (Exception) content;
			return new JRestResult(exception.getClass().getName(), exception
					.getMessage());
		}
		JRestResult jRestResult = new JRestResult(content);
		if (content instanceof Page) {
			Page page = (Page) content;
			jRestResult.setContent(page.getResult());
			jRestResult.setResultCount((int) page.getTotalCount());
			jRestResult.setPageCount((int) page.getTotalPageCount());
			jRestResult.setPageIndex(page.getCurrentPageNo());
		}
		return jRestResult;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
}
