package org.jrest.rest.writer;

import org.jrest.rest.http.HttpResult;

public class StringResponseWriter extends TextResponseWriter {
	@Override
	protected String generateTextContent(Object result) {
		return HttpResult.createHttpResult(result).toTextPlain();
	}

	@Override
	public String getMimeType() {
		return "text/plain,*/*";
	}
}
