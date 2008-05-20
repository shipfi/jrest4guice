package org.jrest4guice.writer;

import org.jrest4guice.annotation.MimeType;
import org.jrest4guice.context.HttpResult;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
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
