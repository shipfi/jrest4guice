package org.jrest4guice.rest.writer;

import org.jrest4guice.rest.ServiceResult;
import org.jrest4guice.rest.annotations.MimeType;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class XmlResponseWriter extends TextResponseWriter {
	@Override
	protected String generateTextContent(Object result) {
		return ServiceResult.createHttpResult(result).toXML();
	}

	@Override
	public String getMimeType() {
		return MimeType.MIME_OF_XML;
	}
}
