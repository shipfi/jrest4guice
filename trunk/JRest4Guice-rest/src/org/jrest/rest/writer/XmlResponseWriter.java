package org.jrest.rest.writer;

import org.jrest.rest.context.HttpResult;

public class XmlResponseWriter extends TextResponseWriter {
	@Override
	protected String generateTextContent(Object result) {
		//TODO -- 实现XML结果的输出
		return HttpResult.createHttpResult(result).toJson();
	}

	@Override
	public String getMimeType() {
		return "application/xml";
	}
}
