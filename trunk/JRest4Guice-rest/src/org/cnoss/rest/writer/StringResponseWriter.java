package org.cnoss.rest.writer;

import org.cnoss.rest.annotation.MimeType;
import org.cnoss.rest.context.HttpResult;

public class StringResponseWriter extends TextResponseWriter {
	@Override
	protected String generateTextContent(Object result) {
		return HttpResult.createHttpResult(result).toTextPlain();
	}

	@Override
	public String getMimeType() {
		return MimeType.MIME_OF_TEXT_PLAIN;
	}
}
