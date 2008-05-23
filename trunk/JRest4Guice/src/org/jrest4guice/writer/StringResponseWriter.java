package org.jrest4guice.writer;

import org.jrest4guice.annotation.MimeType;
import org.jrest4guice.client.JRestResult;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
public class StringResponseWriter extends TextResponseWriter {
	@Override
	protected String generateTextContent(Object result) {
		return JRestResult.createHttpResult(result).toTextPlain();
	}

	@Override
	public String getMimeType() {
		return MimeType.MIME_OF_TEXT_PLAIN;
	}
}
