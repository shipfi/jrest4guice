package org.jrest4guice.writer;

import org.jrest4guice.annotation.MimeType;
import org.jrest4guice.context.HttpResult;

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
