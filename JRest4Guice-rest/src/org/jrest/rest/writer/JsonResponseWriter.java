package org.jrest.rest.writer;

import org.jrest.rest.http.HttpResult;

public class JsonResponseWriter extends TextResponseWriter {
	@Override
	protected String generateTextContent(Object result) {
		return HttpResult.createHttpResult(result).toJson();
	}

	@Override
	public String getMimeType() {
		return "application/json";
	}
}
