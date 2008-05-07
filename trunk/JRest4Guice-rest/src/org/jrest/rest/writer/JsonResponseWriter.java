package org.jrest.rest.writer;

import org.jrest.rest.annotation.MimeType;
import org.jrest.rest.context.HttpResult;

public class JsonResponseWriter extends TextResponseWriter {
	@Override
	protected String generateTextContent(Object result) {
		return HttpResult.createHttpResult(result).toJson();
	}

	@Override
	public String getMimeType() {
		return MimeType.MIME_OF_JSON;
	}
}
