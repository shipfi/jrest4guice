package org.cnoss.rest4guice.writer;

import org.cnoss.rest4guice.annotation.MimeType;
import org.cnoss.rest4guice.context.HttpResult;

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
