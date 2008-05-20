package org.cnoss.rest4guice.writer;

import org.cnoss.rest4guice.annotation.MimeType;
import org.cnoss.rest4guice.context.HttpResult;

public class XmlResponseWriter extends TextResponseWriter {
	@Override
	protected String generateTextContent(Object result) {
		return HttpResult.createHttpResult(result).toXML();
	}

	@Override
	public String getMimeType() {
		return MimeType.MIME_OF_XML;
	}
}
