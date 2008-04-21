package org.jrest.rest.writer;

import org.jrest.rest.http.HttpResult;

public class XmlResponseWriter extends TextResponseWriter {
	@Override
	protected String generateTextContent(Object result) {
		//TODO -- 实现XML结果的输出
		return HttpResult.createSuccessfulHttpResult(result).toJson();
	}

	@Override
	public String getMimeType() {
		return "application/xml";
	}
}
