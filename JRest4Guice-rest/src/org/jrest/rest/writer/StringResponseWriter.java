package org.jrest.rest.writer;

import org.jrest.rest.http.HttpResult;

public class StringResponseWriter extends TextResponseWriter {
	@Override
	protected String generateTextContent(Object result) {
		return HttpResult.createSuccessfulHttpResult(result).toTextPlain();
	}

	@Override
	public String getMimiType() {
		return "text/plain,*/*";
	}
}
