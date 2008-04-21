package org.jrest.rest.writer;

import org.jrest.rest.http.HttpResult;

public class XmlResponseWriter extends TextResponseWriter {
	@Override
	protected String generateTextContent(Object result) {
		return HttpResult.createSuccessfulHttpResult(result).toJson();
	}

	@Override
	public String getMimiType() {
		return "application/xml";
	}
}
