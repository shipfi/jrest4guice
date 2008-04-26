package org.jrest.rest.writer;

import org.jrest.rest.context.HttpResult;

public class XmlResponseWriter extends TextResponseWriter {
	@Override
	protected String generateTextContent(Object result) {
		return HttpResult.createHttpResult(result).toXML();
	}

	@Override
	public String getMimeType() {
		return "application/xml";
	}
}
